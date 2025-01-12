package org.flatscrew.latte.cream.align;

import org.flatscrew.latte.ansi.StringWidth;
import org.flatscrew.latte.cream.Renderer;
import org.flatscrew.latte.cream.TextLines;
import org.flatscrew.latte.cream.color.TerminalColor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import static org.flatscrew.latte.cream.align.Position.Bottom;
import static org.flatscrew.latte.cream.align.Position.Center;
import static org.flatscrew.latte.cream.align.Position.Right;
import static org.flatscrew.latte.cream.align.Position.Top;

public class AlignmentDecorator {

    public static String alignTextVertical(String input, Position position, int height) {
        int strHeight = (int) input.chars().filter(ch -> ch == '\n').count() + 1;
        if (height < strHeight) {
            return input;
        }

        if (position.equals(Top)) {
            return input + "\n".repeat(height - strHeight);
        } else if (position.equals(Center)) {
            int topPadding = (height - strHeight) / 2;
            int bottomPadding = (height - strHeight) / 2;

            if (strHeight + topPadding + bottomPadding > height) {
                topPadding--;
            } else if (strHeight + topPadding + bottomPadding < height) {
                bottomPadding++;
            }
            return "\n".repeat(topPadding) + input + "\n".repeat(bottomPadding);
        } else if (position.equals(Bottom)) {
            return "\n".repeat(height - strHeight) + input;
        }
        return input;
    }

    public static String alignTextHorizontal(String input, Position position, int width, TerminalColor backgroundColor, Renderer renderer) {
        TextLines textLines = TextLines.fromText(input);
        int widestLine = textLines.widestLineLength();

        StringBuilder b = new StringBuilder();
        String[] lines = textLines.lines();
        for (int i = 0; i < lines.length; i++) {
            String l = lines[i];
            int lineWidth = StringWidth.measureWidth(l);

            // difference from the widest line
            int shortAmount = widestLine - lineWidth;
            // difference from the total width, if set
            shortAmount += Math.max(0, width - (shortAmount + lineWidth));

            if (shortAmount > 0) {
                if (position.equals(Right)) {
                    l = " ".repeat(shortAmount) + l;
                } else if (position.equals(Center)) {// Note: remainder goes on the right
                    int left = shortAmount / 2;
                    int right = left + shortAmount % 2;

                    String leftSpaces = " ".repeat(left);
                    String rightSpaces = " ".repeat(right);

                    l = leftSpaces + l + rightSpaces;
                } else {
                    AttributedStyle attributedStyle = new AttributedStyle();
                    attributedStyle = backgroundColor.applyAsBackground(attributedStyle, renderer);

                    l += new AttributedString(" ".repeat(shortAmount), attributedStyle).toAnsi();
                }
            }

            b.append(l);
            if (i < lines.length - 1) {
                b.append('\n');
            }
        }

        return b.toString();
    }
}
