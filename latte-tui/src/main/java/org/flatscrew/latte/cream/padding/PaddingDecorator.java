package org.flatscrew.latte.cream.padding;

public class PaddingDecorator {

    public static String applyPadding(String input,
                                      int topPadding,
                                      int rightPadding,
                                      int bottomPadding,
                                      int leftPadding) {
        String padded = input;
        if (leftPadding > 0) {
            padded = padLeft(padded, leftPadding);
        }
        if (rightPadding > 0) {
            padded = padRight(padded, rightPadding);
        }
        if (topPadding > 0) {
            padded = "\n".repeat(topPadding) + padded;
        }
        if (bottomPadding > 0) {
            padded += "\n".repeat(bottomPadding);
        }
        return padded;
    }

    public static String padLeft(String input, int leftPadding) {
        return pad(input, -leftPadding);
    }

    public static String padRight(String input, int rightPadding) {
        return pad(input, rightPadding);
    }

    public static String pad(String str, int n) {
        if (n == 0) {
            return str;
        }

        String padding = " ".repeat(Math.abs(n));
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
