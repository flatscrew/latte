package org.flatscrew.latte;

import org.flatscrew.latte.ansi.Code;
import org.flatscrew.latte.message.PrintLineMessage;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StandardRenderer implements Renderer {

    private static final int DEFAULT_FPS = 60;

    private volatile boolean needsRender = true;
    private final Lock renderLock = new ReentrantLock();
    private final Terminal terminal;
    private volatile boolean isRunning = false;
    private final StringBuilder buffer = new StringBuilder();
    private volatile String lastRender = "";
    private final ScheduledExecutorService ticker;
    private final long frameTime;
    private String[] lastRenderedLines = new String[0];
    private final List<String> queuedMessageLines = new ArrayList<>();  // Add this field

    private int linesRendered = 0;
    private int width;
    private int height;
    private boolean isInAltScreen;
    private boolean isReportFocus;

    public StandardRenderer(Terminal terminal) {
        this(terminal, DEFAULT_FPS);
    }

    public StandardRenderer(Terminal terminal, int fps) {
        this.terminal = terminal;
        this.frameTime = 1000 / Math.min(Math.max(fps, 1), 120);
        this.ticker = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "Latte-Renderer-Thread");
            t.setDaemon(true);
            return t;
        });

        width = 0;
        try {
            // Get terminal size
            this.width = terminal.getWidth();
            this.height = terminal.getHeight();
        } catch (Exception e) {
            // Fallback to some reasonable defaults if we can't get the size
            this.width = 80;
            this.height = 24;
        }
    }

    public void start() {
        if (!isRunning) {
            isRunning = true;
            ticker.scheduleAtFixedRate(this::flush, 0, frameTime, TimeUnit.MILLISECONDS);
        }
    }

    public void stop() {
        isRunning = false;
        try {
            ticker.shutdownNow();
            ticker.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new ProgramException(e);
        }
    }

    private void flush() {
        if (!needsRender) {
            return;
        }

        renderLock.lock();
        try {
//            if (buffer.isEmpty() || buffer.toString().equals(lastRender)) {
//                return;
//            }

            StringBuilder outputBuffer = new StringBuilder();
            String[] newLines = buffer.toString().split("\n");

            // if height is known and content exceeds it, trim from top
            if (height > 0 && newLines.length > height) {
                newLines = Arrays.copyOfRange(newLines, newLines.length - height, newLines.length);
            }

            // move cursor to start of render area
            if (linesRendered > 1) {
                outputBuffer.append("\033[").append(linesRendered - 1).append("A");
            }

            // first handle queued messages if we're not in alt screen
            boolean flushQueuedMessages = !queuedMessageLines.isEmpty() && !isInAltScreen;
            if (flushQueuedMessages) {
                for (String line : queuedMessageLines) {
                    // add line and erase to end if needed
                    if (width > 0 && line.length() < width) {
                        outputBuffer.append(line).append("\033[K"); // EraseLineRight
                    } else {
                        outputBuffer.append(line);
                    }
                    outputBuffer.append("\r\n");
                }
                queuedMessageLines.clear();
            }

            // Paint new lines
            for (int i = 0; i < newLines.length; i++) {
                boolean canSkip = !flushQueuedMessages && // Skip only if we haven't flushed messages
                        lastRenderedLines.length > i &&
                        newLines[i].equals(lastRenderedLines[i]);

                if (canSkip) {
                    if (i < newLines.length - 1) {
                        outputBuffer.append("\033[B"); // Move down one line
                    }
                    continue;
                }

                // Truncate lines wider than the width of the window to avoid wrapping
                String line = newLines[i];
                if (this.width > 0 && line.length() > width) {
                    line = line.substring(0, this.width);
                }

                // Clear line and write new content
                if (width > 0 && line.length() < width) {
                    outputBuffer.append("\r").append(line).append("\033[K");
                } else {
                    outputBuffer.append("\r").append(line);
                }

                if (i < newLines.length - 1) {
                    outputBuffer.append("\n");
                }
            }

            // Clear any remaining lines from previous render
            if (linesRendered > newLines.length) {
                outputBuffer.append("\033[J"); // Clear screen below
            }

            // Ensure cursor is at the start of the last line
            outputBuffer.append("\r");

            terminal.writer().print(outputBuffer);
            terminal.writer().flush();

            lastRender = buffer.toString();
            lastRenderedLines = newLines;
            linesRendered = newLines.length;
            needsRender = false;
        } finally {
            renderLock.unlock();
        }
    }

    public void write(String view) {
        if (!isRunning) return;

        renderLock.lock();
        try {
            buffer.setLength(0);  // Clear existing buffer
            buffer.append(view);
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void showCursor() {
        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.cursor_visible);
            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void hideCursor() {
        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.cursor_invisible);
            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void enableMouseAllMotion() {
        renderLock.lock();
        try {
            terminal.writer().print(Code.EnableMouseAllMotion.value());
            terminal.writer().flush();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void disableMouseAllMotion() {
        renderLock.lock();
        try {
            terminal.writer().print(Code.DisableMouseAllMotion.value());
            terminal.writer().flush();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void enableMouseSGRMode() {
        renderLock.lock();
        try {
            terminal.writer().print(Code.EnableMouseSgrExt.value());
            terminal.writer().flush();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void disableMouseSGRMode() {
        renderLock.lock();
        try {
            terminal.writer().print(Code.DisableMouseSgrExt.value());
            terminal.writer().flush();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void clearScreen() {
        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.flush();
            repaint();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public boolean altScreen() {
        return isInAltScreen;
    }

    @Override
    public void enterAltScreen() {
        if (isInAltScreen) {
            return;
        }

        renderLock.lock();
        try {
            if (terminal.getType().equals("dumb")) return;

            terminal.puts(InfoCmp.Capability.enter_ca_mode);
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.puts(InfoCmp.Capability.cursor_home);

            // Force a complete repaint when entering alt screen
            repaint();
            needsRender = true;
            isInAltScreen = true;

            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void exitAltScreen() {
        if (!altScreen()) {
            return;
        }

        renderLock.lock();
        try {
            terminal.puts(InfoCmp.Capability.exit_ca_mode);

            // Force a repaint when exiting alt screen
            repaint();
            needsRender = true;
            isInAltScreen = false;

            terminal.flush();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public boolean reportFocus() {
        renderLock.lock();
        try {
            return isReportFocus;
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void enableReportFocus() {
        renderLock.lock();
        try {
            isReportFocus = true;
            terminal.writer().print(Code.EnableFocusReporting.value());
            terminal.writer().flush();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void disableReportFocus() {
        renderLock.lock();
        try {
            isReportFocus = false;
            terminal.writer().print(Code.DisableFocusReporting.value());
            terminal.writer().flush();
        } finally {
            renderLock.unlock();
        }
    }

    @Override
    public void notifyModelChanged() {
        this.needsRender = true;
    }

    @Override
    public void repaint() {
        lastRender = "";
        lastRenderedLines = new String[]{};
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg instanceof PrintLineMessage printLineMessage) {
            if (!isInAltScreen) {
                renderLock.lock();
                try {
                    String[] lines = printLineMessage.messageBody().split("\n");
                    queuedMessageLines.addAll(Arrays.asList(lines));
                    needsRender = true;
                    repaint();
                } finally {
                    renderLock.unlock();
                }
            }
        }
    }
}