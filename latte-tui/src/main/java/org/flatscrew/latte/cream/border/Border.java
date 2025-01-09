package org.flatscrew.latte.cream.border;

import org.flatscrew.latte.ansi.StringWidth;
import org.flatscrew.latte.cream.Renderer;
import org.flatscrew.latte.cream.TextLines;
import org.flatscrew.latte.cream.color.TerminalColor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

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

    public String applyBorders(String string,
                               boolean hasTop,
                               boolean hasRight,
                               boolean hasBottom,
                               boolean hasLeft,
                               TerminalColor borderTopForeground,
                               TerminalColor borderRightForeground,
                               TerminalColor borderBottomForeground,
                               TerminalColor borderLeftForeground,
                               TerminalColor borderTopBackground,
                               TerminalColor borderRightBackground,
                               TerminalColor borderBottomBackground,
                               TerminalColor borderLeftBackground,
                               Renderer renderer) {
        TextLines textLines = TextLines.fromText(string);
        int width = textLines.widestLineLength();

        String top = this.top;
        String bottom = this.bottom;
        String left = this.left;
        String right = this.right;
        String topLeft = this.topLeft;
        String topRight = this.topRight;
        String bottomLeft = this.bottomLeft;
        String bottomRight = this.bottomRight;
        String middleLeft = this.middleLeft;
        String middleRight = this.middleRight;
        String middle = this.middle;
        String middleTop = this.middleTop;
        String middleBottom = this.middleBottom;

        if (hasLeft) {
            if (left == null || left.isEmpty()) {
                left = " ";
            }
            width += StringWidth.measureWidth(left);
        }

        if (hasRight) {
            if (right == null || right.isEmpty()) {
                right = " ";
            }
        }

        if (hasTop && hasLeft && (topLeft == null || topLeft.isEmpty())) {
            topLeft = " ";
        }
        if (hasTop && hasRight && (topRight == null || topRight.isEmpty())) {
            topRight = " ";
        }
        if (hasBottom && hasLeft && (bottomLeft == null || bottomLeft.isEmpty())) {
            bottomLeft = " ";
        }
        if (hasBottom && hasRight && (bottomRight == null || bottomRight.isEmpty())) {
            bottomRight = " ";
        }

        if (hasTop) {
            if (!hasLeft && !hasRight) {
                topLeft = "";
                topRight = "";
            } else if (!hasLeft) {
                topLeft = "";
            } else if (!hasRight) {
                topRight = "";
            }
        }

        if (hasBottom) {
            if (!hasLeft && !hasRight) {
                bottomLeft = "";
                bottomRight = "";
            } else if (!hasLeft) {
                bottomLeft = "";
            } else if (!hasRight) {
                bottomRight = "";
            }
        }

        topLeft = getFirstCharOrEmpty(topLeft);
        topRight = getFirstCharOrEmpty(topRight);
        bottomRight = getFirstCharOrEmpty(bottomRight);
        bottomLeft = getFirstCharOrEmpty(bottomLeft);

        StringBuilder out = new StringBuilder();

        if (hasTop) {
            String topEdge = renderHorizontalEdge(topLeft, top, topRight, width);
            topEdge = styleBorder(topEdge, borderTopForeground, borderTopBackground, renderer);
            out.append(topEdge).append('\n');
        }

        char[] leftRunes = left.toCharArray();
        int leftIndex = 0;

        char[] rightRunes = right.toCharArray();
        int rightIndex = 0;

        int index = 0;
        String[] lines = textLines.lines();
        for (String line : lines) {
            if (hasLeft) {
                String rune = String.valueOf(leftRunes[leftIndex]);
                leftIndex++;

                if (leftIndex >= leftRunes.length) {
                    leftIndex = 0;
                }
                out.append(styleBorder(rune, borderLeftForeground, borderLeftBackground, renderer));
            }

            out.append(line);

            if (hasRight) {
                String rune = String.valueOf(rightRunes[rightIndex]);
                rightIndex++;

                if (rightIndex >= rightRunes.length) {
                    rightIndex = 0;
                }
                out.append(styleBorder(rune, borderRightForeground, borderRightBackground, renderer));
            }

            if (index < lines.length - 1) {
                out.append('\n');
            }
            index++;
        }

        if (hasBottom) {
            String bottomEdge = renderHorizontalEdge(bottomLeft, bottom, bottomRight, width);
            bottomEdge = styleBorder(bottomEdge, borderBottomForeground, borderBottomBackground, renderer);
            out.append('\n').append(bottomEdge);
        }

        return out.toString();
    }

    private String styleBorder(String border, TerminalColor foreground, TerminalColor background, Renderer renderer) {
        AttributedStyle attributedStyle = new AttributedStyle();
        foreground.applyAsBackground(attributedStyle, renderer);
        background.applyAsBackground(attributedStyle, renderer);

        return new AttributedString(border, attributedStyle).toAnsi();
    }

    private String renderHorizontalEdge(String left, String middle, String right, int width) {
        if (middle.isEmpty()) {
            middle = " ";
        }

        int leftWidth = StringWidth.measureWidth(left);
        int rightWidth = StringWidth.measureWidth(right);

        char[] runes = middle.toCharArray();
        int j = 0;

        StringBuilder out = new StringBuilder();
        out.append(left);

        for (int i = leftWidth + rightWidth; i < width + rightWidth; ) {
            out.append(runes[j]);
            j++;
            if (j >= runes.length) {
                j = 0;
            }
            i += StringWidth.measureWidth(String.valueOf(runes[j]));
        }
        out.append(right);

        return out.toString();
    }


    private String getFirstCharOrEmpty(String string) {
        if (string == null || string.isEmpty()) {
            return "";
        }
        return string.substring(0, 1);
    }
}