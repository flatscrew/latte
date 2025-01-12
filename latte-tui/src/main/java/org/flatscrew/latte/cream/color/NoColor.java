package org.flatscrew.latte.cream.color;

import org.flatscrew.latte.cream.Renderer;
import org.jline.utils.AttributedStyle;

public final class NoColor implements TerminalColor{

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer) {
        return style;
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer) {
        return style;
    }
}
