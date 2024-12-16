package org.flatscrew.latte.input;

import org.flatscrew.latte.Message;
import org.flatscrew.latte.ProgramException;
import org.flatscrew.latte.message.KeyPressMessage;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.function.Consumer;

public class InputHandler {

    private final Thread inputThread;
    private final Terminal terminal;
    private final Consumer<Message> messageConsumer;
    private boolean running;

    public InputHandler(Terminal terminal, Consumer<Message> messageConsumer) {
        this.terminal = terminal;
        this.messageConsumer = messageConsumer;

        this.inputThread = new Thread(this::handleInput);
        inputThread.setDaemon(true);
    }

    public void start() {
        this.running = true;
        inputThread.start();
    }

    public void stop() {
        this.running = false;
    }

    private void handleInput() {
        try {
            NonBlockingReader reader = terminal.reader();
            while (running) {
                messageConsumer.accept(new KeyPressMessage(reader.read()));
            }
        } catch (IOException e) {
            throw new ProgramException("Unable to initialize keyboard input", e);
        }
    }
}
