package org.flatscrew.latte.cream.border;

public enum BorderType {
    NoBorder(),
    NormalBorder("─", "─", "│", "│", "┌", "┐", "└", "┘", "├", "┤", "┼", "┬", "┴"),
    RoundedBorder("─", "─", "│", "│", "╭", "╮", "╰", "╯", "├", "┤", "┼", "┬", "┴"),
    BlockBorder("█", "█", "█", "█", "█", "█", "█", "█", "", "", "", "", ""),
    OuterHalfBlockBorder("▀", "▄", "▌", "▐", "▛", "▜", "▙", "▟", "", "", "", "", ""),
    InnerHalfBlockBorder("▄", "▀", "▐", "▌", "▗", "▖", "▝", "▘", "", "", "", "", ""),
    ThickBorder("━", "━", "┃", "┃", "┏", "┓", "┗", "┛", "┣", "┫", "╋", "┳", "┻"),
    DoubleBorder("═", "═", "║", "║", "╔", "╗", "╚", "╝", "╠", "╣", "╬", "╦", "╩"),
    HiddenBorder(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");

    private final String top;
    private final String bottom;
    private final String left;
    private final String right;
    private final String topLeft;
    private final String topRight;
    private final String bottomLeft;
    private final String bottomRight;
    private final String middleLeft;
    private final String middleRight;
    private final String middle;
    private final String middleTop;
    private final String middleBottom;

    BorderType() {
        this("", "", "", "", "", "", "", "", "", "", "", "", "");
    }

    BorderType(String top, String bottom, String left, String right,
               String topLeft, String topRight, String bottomLeft, String bottomRight,
               String middleLeft, String middleRight, String middle, String middleTop,
               String middleBottom) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.middleLeft = middleLeft;
        this.middleRight = middleRight;
        this.middle = middle;
        this.middleTop = middleTop;
        this.middleBottom = middleBottom;
    }
}