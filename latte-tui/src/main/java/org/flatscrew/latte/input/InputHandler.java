package org.flatscrew.latte.input;

import org.flatscrew.latte.Message;
import org.flatscrew.latte.ProgramException;
import org.flatscrew.latte.message.BlurMessage;
import org.flatscrew.latte.message.FocusMessage;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.UnknownSequenceMessage;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {
    private static final Pattern MOUSE_SGR_REGEX = Pattern.compile("(\\d+);(\\d+);(\\d+)([Mm])");
    private final Thread inputThread;
    private final Terminal terminal;
    private final Consumer<Message> messageConsumer;
    private boolean running;

    // Buffer for handling escape sequences
    private final StringBuilder escapeCharactersBuffer = new StringBuilder();
    private static final int ESCAPE_TIMEOUT_MS = 50;

    public InputHandler(Terminal terminal, Consumer<Message> messageConsumer) {
        this.terminal = terminal;
        this.messageConsumer = messageConsumer;
        this.inputThread = new Thread(this::handleInput);
        this.inputThread.setDaemon(true);
    }

    public void start() {
        this.running = true;
        inputThread.start();
    }

    public void stop() {
        this.running = false;
        try {
            inputThread.join(500); // wait up to 500ms for thread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleInput() {
        try {
            NonBlockingReader reader = terminal.reader();
            while (running) {
                int input = reader.read();
                if (input == -1) break;

                if (input == '\u001b') { // ESC character
                    handleEscapeSequence(reader);
                } else {
                    messageConsumer.accept(new KeyPressMessage(input));
                }
            }
        } catch (IOException e) {
            throw new ProgramException("Unable to initialize keyboard input", e);
        }
    }

    private void handleEscapeSequence(NonBlockingReader reader) throws IOException {
        escapeCharactersBuffer.setLength(0);
        escapeCharactersBuffer.append('\u001b');

        // Read the next character with a timeout
        int nextChar = reader.read(ESCAPE_TIMEOUT_MS);

        if (nextChar == -1) {
            // Timeout occurred - it was just an ESC key
            messageConsumer.accept(new KeyPressMessage('\u001b'));
            return;
        }

        escapeCharactersBuffer.append((char) nextChar);

        // Handle focus events
        if (nextChar == '[') {
            nextChar = reader.read(ESCAPE_TIMEOUT_MS);
            if (nextChar != -1) {
                escapeCharactersBuffer.append((char) nextChar);

                if (nextChar == 'I') {
                    messageConsumer.accept(new FocusMessage()); // Focus gained
                    return;
                } else if (nextChar == 'O') {
                    messageConsumer.accept(new BlurMessage()); // Focus lost
                    return;
                } else if (nextChar == 'M') {
                    handleX10MouseEvent(reader);
                    return;
                } else if (nextChar == '<') {
                    handleSGRMouseEvent(reader);
                    return;
                }
            }
        }

        // If we got here, it's either an Alt+key combination or an unknown sequence
        handleRemainingSequence(reader);
    }

    private void handleX10MouseEvent(NonBlockingReader reader) throws IOException {
        // Read 3 bytes for button and coordinates
        int button = reader.read() - 32;
        int col = reader.read() - 32;
        int row = reader.read() - 32;

        messageConsumer.accept(new MouseMessage(
                MouseMessage.Type.X10,
                button,
                col,
                row,
                false  // not an SGR event
        ));
    }

    private void handleSGRMouseEvent(NonBlockingReader reader) throws IOException {
        escapeCharactersBuffer.setLength(0);

        // Read until we find 'M' or 'm'
        int ch;
        while ((ch = reader.read(ESCAPE_TIMEOUT_MS)) != -1) {
            char c = (char) ch;
            escapeCharactersBuffer.append(c);
            if (c == 'M' || c == 'm') {
                break;
            }
        }

        Matcher matcher = MOUSE_SGR_REGEX.matcher(escapeCharactersBuffer.toString());
        if (matcher.matches()) {
            int button = Integer.parseInt(matcher.group(1));
            int col = Integer.parseInt(matcher.group(2));
            int row = Integer.parseInt(matcher.group(3));
            boolean release = matcher.group(4).equals("m");

            messageConsumer.accept(new MouseMessage(
                    MouseMessage.Type.SGR,
                    button,
                    col,
                    row,
                    release
            ));
        }
    }

    private void handleRemainingSequence(NonBlockingReader reader) throws IOException {
        // Read any remaining characters in the sequence with timeout
        int ch;
        while ((ch = reader.read(ESCAPE_TIMEOUT_MS)) != -1) {
            escapeCharactersBuffer.append((char) ch);
        }

        // Process the complete sequence
        String sequence = escapeCharactersBuffer.toString();
        if (sequence.length() == 2 && sequence.charAt(0) == '\u001b') {
            // Alt + key combination
            messageConsumer.accept(new KeyPressMessage(sequence.charAt(1), true));
        } else {
            // Unknown sequence - you might want to handle this differently
            messageConsumer.accept(new UnknownSequenceMessage(sequence));
        }
    }
}