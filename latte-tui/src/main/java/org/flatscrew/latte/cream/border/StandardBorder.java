package org.flatscrew.latte.cream.border;

public class StandardBorder {

    public static Border NoBorder = new Border("", "", "", "", "", "", "", "", "", "", "", "", "");
    public static Border NormalBorder = new Border("─", "─", "│", "│", "┌", "┐", "└", "┘", "├", "┤", "┼", "┬", "┴");
    public static Border RoundedBorder = new Border("─", "─", "│", "│", "╭", "╮", "╰", "╯", "├", "┤", "┼", "┬", "┴");
    public static Border BlockBorder = new Border("█", "█", "█", "█", "█", "█", "█", "█", "", "", "", "", "");
    public static Border OuterHalfBlockBorder = new Border("▀", "▄", "▌", "▐", "▛", "▜", "▙", "▟", "", "", "", "", "");
    public static Border InnerHalfBlockBorder = new Border("▄", "▀", "▐", "▌", "▗", "▖", "▝", "▘", "", "", "", "", "");
    public static Border ThickBorder = new Border("━", "━", "┃", "┃", "┏", "┓", "┗", "┛", "┣", "┫", "╋", "┳", "┻");
    public static Border DoubleBorder = new Border("═", "═", "║", "║", "╔", "╗", "╚", "╝", "╠", "╣", "╬", "╦", "╩");
    public static Border HiddenBorder = new Border(" ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ", " ");
}
