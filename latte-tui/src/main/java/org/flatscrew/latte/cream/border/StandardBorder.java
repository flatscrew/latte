package org.flatscrew.latte.cream.border;

public class StandardBorder {

    public static Border noBorder() {
        return new Border("",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "");
    }

    public static Border normalBorder() {
        return new Border("─",
                "─",
                "│",
                "│",
                "┌",
                "┐",
                "└",
                "┘",
                "├",
                "┤",
                "┼",
                "┬",
                "┴");
    }

    public static Border roundedBorder() {
        return new Border("─",
                "─",
                "│",
                "│",
                "╭",
                "╮",
                "╰",
                "╯",
                "├",
                "┤",
                "┼",
                "┬",
                "┴");
    }

    public static Border blockBorder() {
        return new Border("█",
                "█",
                "█",
                "█",
                "█",
                "█",
                "█",
                "█",
                "",
                "",
                "",
                "",
                "");
    }

    public static Border outerHalfBlockBorder() {
        return new Border("▀",
                "▄",
                "▌",
                "▐",
                "▛",
                "▜",
                "▙",
                "▟",
                "",
                "",
                "",
                "",
                "");
    }

    public static Border innerHalfBlockBorder() {
        return new Border("▄",
                "▀",
                "▐",
                "▌",
                "▗",
                "▖",
                "▝",
                "▘",
                "",
                "",
                "",
                "",
                "");
    }

    public static Border thickBorder() {
        return new Border("━",
                "━",
                "┃",
                "┃",
                "┏",
                "┓",
                "┗",
                "┛",
                "┣",
                "┫",
                "╋",
                "┳",
                "┻");
    }

    public static Border doubleBorder() {
        return new Border("═",
                "═",
                "║",
                "║",
                "╔",
                "╗",
                "╚",
                "╝",
                "╠",
                "╣",
                "╬",
                "╦",
                "╩");
    }

    public static Border hiddenBorder() {
        return new Border(" ",
                " ",
                " ",
                " ",
                " ",
                " ",
                " ",
                " ",
                " ",
                " ",
                " ",
                " ",
                " ");
    }
}
