package org.flatscrew.latte.cream.join;

import org.flatscrew.latte.ansi.TextWidth;
import org.flatscrew.latte.cream.Position;
import org.flatscrew.latte.cream.TextLines;

import java.util.Arrays;

import static org.flatscrew.latte.cream.Position.Bottom;
import static org.flatscrew.latte.cream.Position.Top;

public class HorizontalJoinDecorator {

    public static String joinHorizontal(Position position, String... strings) {

        if (strings.length == 0) {
            return "";
        }
        if (strings.length == 1) {
            return strings[0];
        }

        String[][] blocks = new String[strings.length][];
        int[] maxWidths = new int[strings.length];
        int maxHeight = 0;

        for (int i = 0; i < strings.length; i++) {
            String str = strings[i];
            TextLines textLines = TextLines.fromText(str);
            blocks[i] = textLines.lines();
            maxWidths[i] = textLines.widestLineLength();

            if (blocks[i].length > maxHeight) {
                maxHeight = blocks[i].length;
            }
        }

        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i].length >= maxHeight) {
                continue;
            }

            String[] extraLines = new String[maxHeight - blocks[i].length];
            Arrays.fill(extraLines, "");

            if (position.equals(Top)) {
                String[] newBlock = new String[maxHeight];
                System.arraycopy(blocks[i], 0, newBlock, 0, blocks[i].length);
                System.arraycopy(extraLines, 0, newBlock, blocks[i].length, extraLines.length);
                blocks[i] = newBlock;
            } else if (position.equals(Bottom)) {
                String[] newBlock = new String[maxHeight];
                System.arraycopy(extraLines, 0, newBlock, 0, extraLines.length);
                System.arraycopy(blocks[i], 0, newBlock, extraLines.length, blocks[i].length);
                blocks[i] = newBlock;
            } else {
                int n = extraLines.length;
                int split = (int) Math.round(n * position.value());
                int top = n - split;
                int bottom = n - top;

                String[] newBlock = new String[maxHeight];
                Arrays.fill(newBlock, "");
                System.arraycopy(extraLines, top, newBlock, 0, n - top);
                System.arraycopy(blocks[i], 0, newBlock, n - top, blocks[i].length);
                System.arraycopy(extraLines, 0, newBlock, n - top + blocks[i].length, bottom);

                blocks[i] = newBlock;
            }
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < blocks[0].length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                String[] block = blocks[j];

                builder.append(block[i]);
                builder.append(" ".repeat(maxWidths[j] - TextWidth.measureCellWidth(block[i])));
            }
            if (i < blocks[0].length - 1) {
                builder.append('\n');
            }
        }

        return builder.toString();
    }
}