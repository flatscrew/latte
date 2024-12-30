package org.flatscrew.latte.cream.color;

import org.jline.utils.AttributedStyle;

import static org.flatscrew.latte.cream.Renderer.defaultRenderer;

public class Color implements TerminalColor {

    public static Color color(String color) {
        return new Color(color);
    }

    private final String color;

    private Color(String color) {
        this.color = color;
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style) {
        return defaultRenderer()
                .colorProfile()
                .color(color)
                .applyAsBackground(style);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style) {
        return defaultRenderer()
                .colorProfile()
                .color(color)
                .applyAsForeground(style);
    }
}
