package org.flatscrew.latte.cream;

import org.flatscrew.latte.ansi.StringWidth;

public class TextLines {

    private int widestLineLength;
    private String[] lines;

    public static TextLines fromText(String text) {
        return new TextLines(text);
    }

    private TextLines(String text) {
        readTextLines(text);
    }

    private void readTextLines(String text) {
        this.lines = text.split("\n");

        for (String line : lines) {
            int width = StringWidth.measureWidth(line);
            if (widestLineLength < width) {
                widestLineLength = width;
            }
        }
    }

    public int widestLineLength() {
        return widestLineLength;
    }

    public String[] lines() {
        return lines;
    }
}
