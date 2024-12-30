package org.flatscrew.latte.cream.color;

import org.jline.utils.AttributedStyle;

public class NoColor implements TerminalColor{

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style) {
        return style;
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style) {
        return style;
    }
}
