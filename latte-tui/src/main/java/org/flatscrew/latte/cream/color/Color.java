package org.flatscrew.latte.cream.color;

import org.flatscrew.latte.cream.Renderer;
import org.jline.utils.AttributedStyle;

public final class Color implements TerminalColor {

    public static Color color(String color) {
        return new Color(color);
    }

    private final String color;

    private Color(String color) {
        this.color = color;
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer) {
        return renderer
                .colorProfile()
                .color(color)
                .applyAsBackground(style, renderer);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer) {
        return renderer
                .colorProfile()
                .color(color)
                .applyAsForeground(style, renderer);
    }
}
