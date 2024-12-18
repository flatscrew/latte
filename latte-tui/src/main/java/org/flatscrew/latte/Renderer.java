package org.flatscrew.latte;

public interface Renderer {

    void start();
    void stop();
    void write(String view);
    void showCursor();
    void hideCursor();
    void clearScreen();
    boolean altScreen();
    void enterAltScreen();
    void exitAltScreen();
    boolean reportFocus();
    void notifyModelChanged();
    void disableReportFocus();
    void enableReportFocus();
    void repaint();
}
