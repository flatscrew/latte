package org.flatscrew.latte.cream.margin;

import org.flatscrew.latte.cream.color.TerminalColor;

import static org.flatscrew.latte.cream.padding.PaddingDecorator.padLeft;
import static org.flatscrew.latte.cream.padding.PaddingDecorator.padRight;

public class MarginDecorator {

    public static String applyMargins(String input,
                                      int topMargin,
                                      int rightMargin,
                                      int bottomMargin,
                                      int leftMargin,
                                      TerminalColor backgroundColor) {
        String padded = input;
        if (leftMargin > 0) {
            padded = padLeft(padded, leftMargin);
        }
        if (rightMargin > 0) {
            padded = padRight(padded, rightMargin);
        }


        int width = getLines(input);


        if (topMargin > 0) {
            padded = "\n".repeat(topMargin) + padded;
        }
        if (bottomMargin > 0) {
            padded += "\n".repeat(bottomMargin);
        }
        return padded;
    }

    private static int getLines(String input) {
        return 0;
    }
}