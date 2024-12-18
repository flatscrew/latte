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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {
    private static final Pattern MOUSE_SGR_REGEX = Pattern.compile("(\\d+);(\\d+);(\\d+)([Mm])");
    private final Terminal terminal;
    private final Consumer<Message> messageConsumer;
    private volatile boolean running;
    private final ExecutorService inputExecutor;

    // Buffer for handling escape sequences
    private final StringBuilder escapeCharactersBuffer = new StringBuilder();
    private static final int ESCAPE_TIMEOUT_MS = 50;

    public InputHandler(Terminal terminal, Consumer<Message> messageConsumer) {
        this.terminal = terminal;
        this.messageConsumer = messageConsumer;
        this.inputExecutor = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, "Latte-Input-Thread");
            t.setDaemon(true);
            return t;
        });
    }

    public void start() {
        if (!running) {
            running = true;
            inputExecutor.submit(this::handleInput);
        }
    }

    public void stop() {
        running = false;
        inputExecutor.shutdownNow();
        try {
            inputExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
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
                    StringBuilder sequence = new StringBuilder();
                    sequence.append((char)input);

                    // Look ahead for [ and next character
                    int nextChar = reader.read(ESCAPE_TIMEOUT_MS);
                    if (nextChar != -1) {
                        sequence.append((char)nextChar);

                        if (nextChar == '[') {
                            int thirdChar = reader.read(ESCAPE_TIMEOUT_MS);
                            if (thirdChar != -1) {
                                sequence.append((char)thirdChar);

                                // Handle known special cases
                                if (thirdChar == 'I') {
                                    messageConsumer.accept(new FocusMessage());
                                    continue;
                                } else if (thirdChar == 'O') {
                                    messageConsumer.accept(new BlurMessage());
                                    continue;
                                } else if (thirdChar == 'M') {
                                    handleX10MouseEvent(reader);
                                    continue;
                                } else if (thirdChar == '<') {
                                    handleSGRMouseEvent(reader);
                                    continue;
                                } else if (thirdChar == '?') {
                                    // This is definitely an unknown control sequence
                                    // Read the rest of the sequence
                                    int ch;
                                    while ((ch = reader.read(ESCAPE_TIMEOUT_MS)) != -1) {
                                        sequence.append((char)ch);
                                    }
                                    messageConsumer.accept(new UnknownSequenceMessage(sequence.toString()));
                                    continue;
                                }
                                // For arrow keys and other known sequences
                                messageConsumer.accept(new KeyPressMessage(thirdChar));
                                continue;
                            }
                        } else {
                            // Handle Alt+key combination
                            messageConsumer.accept(new KeyPressMessage(nextChar, true));
                            continue;
                        }
                    }
                    messageConsumer.accept(new KeyPressMessage(input));
                    continue;
                }
                messageConsumer.accept(new KeyPressMessage(input));
            }
        } catch (IOException e) {
            if (!Thread.currentThread().isInterrupted()) {
                throw new ProgramException("Unable to initialize keyboard input", e);
            }
        }
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
}