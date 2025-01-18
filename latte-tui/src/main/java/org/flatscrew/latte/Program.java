package org.flatscrew.latte;

import org.flatscrew.latte.input.InputHandler;
import org.flatscrew.latte.message.BatchMessage;
import org.flatscrew.latte.message.CheckWindowSizeMessage;
import org.flatscrew.latte.message.ClearScreenMessage;
import org.flatscrew.latte.message.EnterAltScreen;
import org.flatscrew.latte.message.ErrorMessage;
import org.flatscrew.latte.message.ExitAltScreen;
import org.flatscrew.latte.message.QuitMessage;
import org.flatscrew.latte.message.SequenceMessage;
import org.flatscrew.latte.message.WindowSizeMessage;
import org.flatscrew.latte.term.TerminalInfo;
import org.flatscrew.latte.term.jline.JLineTerminalInfoProvider;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.Signals;

import java.io.IOException;
import java.util.Arrays;
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

    private Throwable lastError;
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

            // terminal info provider here
            TerminalInfo.provide(new JLineTerminalInfoProvider(terminal));

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

    public Program withReportFocus() {
        renderer.enableReportFocus();
        return this;
    }

    public Program withMouseAllMotion() {
        renderer.enableMouseAllMotion();
        renderer.enableMouseSGRMode();
        return this;
    }

    public void run() {
        if (!isRunning.compareAndSet(false, true)) {
            throw new IllegalStateException("Program is already running!");
        }

        handleTerminationSignals();
        handleTerminalResize();

        // start reading keyboard input
        inputHandler.start();

        // starting renderer
        renderer.hideCursor();
        renderer.start();

        // execute init command
        Command initCommand = currentModel.init();
        commandExecutor
                .executeIfPresent(initCommand, this::send, this::sendError)
                .thenRun(initLatch::countDown);

        // render the initial view
        renderer.write(currentModel.view());

        // run event loop
        Model finalModel = eventLoop();

        // stop reading keyboard input
        inputHandler.stop();

        // render final model view before closing
        renderer.write(finalModel.view());
        renderer.showCursor();
        renderer.stop();

        // disabling mouse support
        disableMouse();

        if (renderer.reportFocus()) {
            renderer.disableReportFocus();
        }

        if (renderer.altScreen()) {
            renderer.exitAltScreen();
        }

        terminal.puts(InfoCmp.Capability.carriage_return);
        terminal.puts(InfoCmp.Capability.cursor_down);
        terminal.flush();

        // Finally clean up
        isRunning.set(false);
        commandExecutor.shutdown();
        try {
            terminal.close();
        } catch (IOException e) {
            throw new ProgramException(e);
        }

        if (lastError != null) {
            throw new ProgramException(lastError);
        }
    }

    private void handleTerminationSignals() {
        Signals.register("INT", () -> commandExecutor.executeIfPresent(QuitMessage::new, this::send, this::sendError));
        Signals.register("TERM", () -> commandExecutor.executeIfPresent(QuitMessage::new, this::send, this::sendError));
    }

    private void handleTerminalResize() {
        Signals.register("WINCH", () -> commandExecutor.executeIfPresent(CheckWindowSizeMessage::new, this::send, this::sendError));
        commandExecutor.executeIfPresent(CheckWindowSizeMessage::new, this::send, this::sendError);
    }

    private Model eventLoop() {
        while (isRunning.get()) {
            Message msg;
            try {
                msg = messageQueue.poll(10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            if (msg != null) {
                if (msg instanceof ClearScreenMessage) {
                    renderer.clearScreen();
                    continue;
                } else if (msg instanceof QuitMessage) {
                    return currentModel;
                } else if (msg instanceof EnterAltScreen) {
                    renderer.enterAltScreen();
                    continue;
                } else if (msg instanceof ExitAltScreen) {
                    renderer.exitAltScreen();
                    continue;
                } else if (msg instanceof BatchMessage batchMessage) {
                    Arrays.stream(batchMessage.commands())
                            .forEach(command -> commandExecutor.executeIfPresent(command, this::send, this::sendError));
                } else if (msg instanceof SequenceMessage sequenceMessage) {
                    Arrays.stream(sequenceMessage.commands())
                            .reduce(
                                    CompletableFuture.completedFuture(null),
                                    (CompletableFuture<Void> future, Command command) ->
                                            future.thenCompose(__ ->
                                                    commandExecutor.executeIfPresent(command, this::send, this::sendError)
                                            ),
                                    (f1, f2) -> f1.thenCompose(__ -> f2)
                            ).join();
                } else if (msg instanceof ErrorMessage errorMessage) {
                    this.lastError = errorMessage.error();
                    return currentModel;
                } else if (msg instanceof CheckWindowSizeMessage) {
                    commandExecutor.executeIfPresent(this::checkSize, this::send, this::sendError);
                }

                // process internal messages for the renderer
                renderer.handleMessage(msg);

                UpdateResult<? extends Model> updateResult = currentModel.update(msg);

                currentModel = updateResult.model();
                renderer.notifyModelChanged();
                commandExecutor.executeIfPresent(updateResult.command(), this::send, this::sendError);
            }
            renderer.write(currentModel.view());
        }
        return currentModel;
    }

    private Message checkSize() {
        Size size = terminal.getSize();
        return new WindowSizeMessage(size.getColumns(), size.getRows());
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

    private void disableMouse() {
        renderer.disableMouseSGRMode();
        renderer.disableMouseAllMotion();
    }
}
