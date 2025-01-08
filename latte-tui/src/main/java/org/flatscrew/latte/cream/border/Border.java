package org.flatscrew.latte.cream.border;

import org.flatscrew.latte.ansi.StringWidth;

public record Border(
        String top,
        String bottom,
        String left,
        String right,
        String topLeft,
        String topRight,
        String bottomLeft,
        String bottomRight,
        String middleLeft,
        String middleRight,
        String middle,
        String middleTop,
        String middleBottom
) {

    public int getTopSize() {
        return getBorderEdgeWidth(topLeft, top, topRight);
    }

    public int getRightSize() {
        return getBorderEdgeWidth(topRight, right, bottomRight);
    }

    public int getBottomSize() {
        return getBorderEdgeWidth(bottomLeft, bottom, bottomRight);
    }

    public int getLeftSize() {
        return getBorderEdgeWidth(topLeft, left, bottomLeft);
    }

    private int getBorderEdgeWidth(String... borderParts) {
        int maxWidth = 0;
        for (String piece : borderParts) {
            int width = StringWidth.measureWidth(piece);
            if (width > maxWidth) {
                maxWidth = width;
            }
        }
        return maxWidth;
    }
}