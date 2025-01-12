package org.flatscrew.latte.cream.join;

import org.flatscrew.latte.ansi.TextWidth;
import org.flatscrew.latte.cream.TextLines;
import org.flatscrew.latte.cream.Position;

public class VerticalJoinDecorator {

    public static String joinVertical(Position position, String... strings) {
        if (strings.length == 0) {
            return "";
        }
        if (strings.length == 1) {
            return strings[0];
        }

        String[][] blocks = new String[strings.length][];
        int maxWidth = 0;

        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            TextLines textLines = TextLines.fromText(string);
            blocks[i] = textLines.lines();
            if (textLines.widestLineLength() > maxWidth) {
                maxWidth = textLines.widestLineLength();
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < blocks.length; i++) {
            String[] block = blocks[i];

            for (int j = 0; j < block.length; j++) {
                String line = block[j];
                int width = maxWidth - TextWidth.measureCellWidth(line);

                if (position.equals(Position.Left)) {
                    builder.append(line);
                    builder.append(" ".repeat(width));

                } else if (position.equals(Position.Right)) {
                    builder.append(" ".repeat(width));
                    builder.append(line);
                } else {
                    if (width < 1) {
                        builder.append(line);
                    } else {
                        int split = (int) Math.round(width * position.value());
                        int right = width - split;
                        int left = width - right;

                        builder.append(" ".repeat(left));
                        builder.append(line);
                        builder.append(" ".repeat(right));
                    }
                }
                if (!(i == blocks.length - 1 && j == block.length - 1)) {
                    builder.append('\n');
                }
            }
        }
        return builder.toString();
    }
}
