package org.flatscrew.latte;

import org.flatscrew.latte.input.InputHandler;
import org.flatscrew.latte.message.BatchMessage;
import org.flatscrew.latte.message.EnterAltScreen;
import org.flatscrew.latte.message.ErrorMessage;
import org.flatscrew.latte.message.ExitAltScreen;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.message.SequenceMessage;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.Signals;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Program {

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final CountDownLatch initLatch = new CountDownLatch(1);
    private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    private final CommandExecutor commandExecutor;
    private final InputHandler inputHandler;

    private volatile Model currentModel;
    private final Renderer renderer;
    private final Terminal terminal;

    public Program(Model initialModel) {
        this.currentModel = initialModel;
        this.commandExecutor = new CommandExecutor();

        try {
            this.terminal = TerminalBuilder.builder()
                    .system(true)
                    .jni(true)
                    .build();
            terminal.enterRawMode();

            this.renderer = new StandardRenderer(terminal);
            this.inputHandler = new InputHandler(terminal, this::send);
        } catch (IOException e) {
            throw new ProgramException("Failed to initialize terminal", e);
        }
    }

    public Program withAltScreen() {
        renderer.enterAltScreen();
        return this;
    }


    public void run() {
        if (!isRunning.compareAndSet(false, true)) {
            throw new IllegalStateException("Program is already running!");
        }

        handleTerminationSignals();

        // start reading keyboard input
        inputHandler.start();

        // run event loop
        Model finalModel = eventLoop();

        // stop reading keyboard input
        inputHandler.stop();

        // render final model view before closing
        renderer.write(finalModel.view());
        renderer.stop();
        renderer.showCursor();

        if (renderer.altScreen()) {
            renderer.exitAltScreen();
        }

        terminal.puts(InfoCmp.Capability.carriage_return);
        terminal.puts(InfoCmp.Capability.cursor_down);
        terminal.flush();

        // Finally clean up
        isRunning.set(false);
        commandExecutor.shutdown();
    }

    private void handleTerminationSignals() {
        Signals.register("INT", () -> send(new QuitMessage()));
        Signals.register("TERM", () -> send(new QuitMessage()));
    }

    private Model eventLoop() {
        Command initCommand = currentModel.init();
        commandExecutor.executeIfPresent(initCommand, this::send, this::sendError);

        renderer.hideCursor();
        renderer.start();
        initLatch.countDown();

        while (isRunning.get()) {
            try {
                Message msg = messageQueue.poll(50, TimeUnit.MILLISECONDS);
                if (msg != null) {
                    if (msg instanceof QuitMessage) {
                        return currentModel;
                    } else if (msg instanceof EnterAltScreen) {
                        renderer.enterAltScreen();
                        continue;
                    } else if (msg instanceof ExitAltScreen) {
                        renderer.exitAltScreen();
                        continue;
                    } else if (msg instanceof BatchMessage batchMessage) {
                        for (Command command : batchMessage.commands()) {
                            commandExecutor.executeIfPresent(command, this::send, this::sendError);
                        }
                    } else if (msg instanceof SequenceMessage sequenceMessage) {
                        CompletableFuture<Void> sequence = CompletableFuture.completedFuture(null);
                        for (Command command : sequenceMessage.commands()) {
                            sequence = sequence.thenCompose(ignored ->
                                    commandExecutor.executeIfPresent(command, this::send, this::sendError)
                            );
                        }
                    } else if (msg instanceof ErrorMessage errorMessage) {
                        throw new ProgramException(errorMessage.error());
                    }

                    UpdateResult<? extends Model> updateResult = currentModel.update(msg);

                    currentModel = updateResult.model();
                    renderer.notifyModelChanged();
                    commandExecutor.executeIfPresent(updateResult.command(), this::send, this::sendError);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            renderer.write(currentModel.view());
        }
        return currentModel;
    }

    private void sendError(Throwable error) {
        send(new ErrorMessage(error));
    }

    public void send(Message msg) {
        if (isRunning.get()) {
            messageQueue.offer(msg);
        }
    }

    public void waitForInit() {
        try {
            initLatch.await();
        } catch (InterruptedException e) {
            throw new ProgramException(e);
        }
    }
}
