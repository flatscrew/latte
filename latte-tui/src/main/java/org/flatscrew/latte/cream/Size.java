package org.flatscrew.latte.cream;

import org.flatscrew.latte.ansi.TextWidth;

public class Size {

    public static int width(String input) {
        int currWidth = 0;
        String[] strings = input.split("\n");
        for (String string : strings) {
            int cellWidth = TextWidth.measureCellWidth(string);
            if (cellWidth > currWidth) {
                currWidth = cellWidth;
            }
        }
        return currWidth;
    }

    public static int height(String input) {
        return (int) (input.chars().filter(ch -> ch == '\n').count() + 1);
    }
}
