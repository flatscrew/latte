package org.flatscrew.latte.cream.padding;

import org.flatscrew.latte.cream.Renderer;
import org.flatscrew.latte.cream.color.TerminalColor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

public class PaddingDecorator {

    public static String applyPadding(String input,
                                      int topPadding,
                                      int rightPadding,
                                      int bottomPadding,
                                      int leftPadding,
                                      TerminalColor backgroundColor,
                                      Renderer renderer) {
        String padded = input;
        if (leftPadding > 0) {
            padded = padLeft(padded, leftPadding, backgroundColor, renderer);
        }
        if (rightPadding > 0) {
            padded = padRight(padded, rightPadding, backgroundColor, renderer);
        }
        if (topPadding > 0) {
            padded = "\n".repeat(topPadding) + padded;
        }
        if (bottomPadding > 0) {
            padded += "\n".repeat(bottomPadding);
        }
        return padded;
    }

    public static String padLeft(String input, int leftPadding, TerminalColor backgroundColor, Renderer renderer) {
        return pad(input, -leftPadding, backgroundColor, renderer);
    }

    public static String padRight(String input, int rightPadding, TerminalColor backgroundColor, Renderer renderer) {
        return pad(input, rightPadding, backgroundColor, renderer);
    }

    public static String pad(String str, int n, TerminalColor backgroundColor, Renderer renderer) {
        if (n == 0) {
            return str;
        }
        String padding = " ".repeat(Math.abs(n));

        if (backgroundColor != null) {
            AttributedStyle attributedStyle = new AttributedStyle();
            attributedStyle = backgroundColor.applyAsBackground(attributedStyle, renderer);
            padding = new AttributedString(padding, attributedStyle).toAnsi();
        }

        StringBuilder b = new StringBuilder();
        String[] lines = str.split("\n");

        for (int i = 0; i < lines.length; i++) {
            // pad right
            if (n > 0) {
                b.append(lines[i]);
                b.append(padding);
            }
            // pad left
            else {
                b.append(padding);
                b.append(lines[i]);
            }

            if (i != lines.length - 1) {
                b.append('\n');
            }
        }
        return b.toString();
    }
}
