package org.flatscrew.latte.cream.color;

import org.jline.utils.AttributedStyle;

public class ANSIColor implements TerminalColor {

    private final ColorApplyStrategy applyStrategy;

    // ANSIColor is a color (0-15) as defined by the ANSI Standard.
    public ANSIColor(int colorCode) {
        this.applyStrategy = new ColorCodeApplyStrategy(colorCode);
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style) {
        return applyStrategy.applyForBackground(style);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style) {
        return applyStrategy.applyForForeground(style);
    }
}
