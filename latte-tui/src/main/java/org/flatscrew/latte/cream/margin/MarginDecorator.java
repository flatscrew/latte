package org.flatscrew.latte.cream.margin;

import org.flatscrew.latte.cream.Renderer;
import org.flatscrew.latte.cream.TextLines;
import org.flatscrew.latte.cream.color.TerminalColor;

import static org.flatscrew.latte.cream.padding.PaddingDecorator.padLeft;
import static org.flatscrew.latte.cream.padding.PaddingDecorator.padRight;

public class MarginDecorator {

    public static String applyMargins(String input,
                                      int topMargin,
                                      int rightMargin,
                                      int bottomMargin,
                                      int leftMargin,
                                      TerminalColor marginBackgroundColor,
                                      Renderer renderer) {
        String padded = input;
        if (leftMargin > 0) {
            padded = padLeft(padded, leftMargin, marginBackgroundColor, renderer);
        }
        if (rightMargin > 0) {
            padded = padRight(padded, rightMargin, marginBackgroundColor, renderer);
        }

        int width = TextLines.fromText(input).widestLineLength();
        String spaces = " ".repeat(width);
        if (topMargin > 0) {
            padded = (spaces + "\n").repeat(topMargin) + padded;
        }
        if (bottomMargin > 0) {
            padded += ("\n" + spaces).repeat(bottomMargin);
        }
        return padded;
    }
}