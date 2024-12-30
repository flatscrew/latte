package org.flatscrew.latte.cream.color;

import org.jline.utils.AttributedStyle;

import static org.flatscrew.latte.cream.Renderer.defaultRenderer;

public class Color implements TerminalColor {

    public static Color color(String color) {
        if (color == null || color.isBlank()) {
            return null;
        }
        if (color.startsWith("#")) {

        }
        return new Color(color);
    }

    private Color(String color) {
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style) {
        defaultRenderer().colorProfile();
        return null;
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style) {
        return null;
    }
}
