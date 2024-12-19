package org.flatscrew.latte;

public interface Renderer {
    // Start the renderer.
    void start();
    void stop();

    void write(String view);
    void repaint();
    void clearScreen();

    boolean altScreen();
    void enterAltScreen();
    void exitAltScreen();

    void showCursor();
    void hideCursor();

    // enableMouseAllMotion enables mouse click, release, wheel and motion
    // events, regardless of whether a mouse button is pressed. Many modern
    // terminals support this, but not all.
    void enableMouseAllMotion();
    // disableMouseAllMotion disables All Motion mouse tracking.
    void disableMouseAllMotion();
    // enableMouseSGRMode enables mouse extended mode (SGR).
    void enableMouseSGRMode();
    // disableMouseSGRMode disables mouse extended mode (SGR).
    void disableMouseSGRMode();

    // reportFocus returns whether reporting focus events is enabled.
    boolean reportFocus();
    // enableReportFocus reports focus events to the program.
    void enableReportFocus();

    // disableReportFocus stops reporting focus events to the program.
    void disableReportFocus();

    void notifyModelChanged();

    void handleMessage(Message msg);
}
