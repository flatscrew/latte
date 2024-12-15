package org.flatscrew.latte.cream;
import org.jline.utils.AttributedStyle;

interface ColorApplyStrategy {
    AttributedStyle applyForForeground(AttributedStyle attributedStyle);
    AttributedStyle applyForBackground(AttributedStyle attributedStyle);
}

class ColorCodeApplyStrategy implements ColorApplyStrategy {

    private final int colorCode;

    ColorCodeApplyStrategy(int colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public AttributedStyle applyForForeground(AttributedStyle attributedStyle) {
        return attributedStyle.foreground(colorCode);
    }

    @Override
    public AttributedStyle applyForBackground(AttributedStyle attributedStyle) {
        return attributedStyle.background(colorCode);
    }
}

class RGBAApplyStrategy implements ColorApplyStrategy {

    private final int r;
    private final int g;
    private final int b;

    public RGBAApplyStrategy(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public AttributedStyle applyForForeground(AttributedStyle attributedStyle) {
        return attributedStyle.foreground(r, g, b);
    }

    @Override
    public AttributedStyle applyForBackground(AttributedStyle attributedStyle) {
        return attributedStyle.background(r, g, b);
    }
}

public class Color {

    private final ColorApplyStrategy applyStrategy;

    public Color(int colorCode) {
        this.applyStrategy = new ColorCodeApplyStrategy(colorCode);
    }

    public Color(int r, int g, int b) {
        this.applyStrategy = new RGBAApplyStrategy(r, g, b);
    }

    public AttributedStyle applyAsBackground(AttributedStyle style) {
        return applyStrategy.applyForBackground(style);
    }

    public AttributedStyle applyAsForeground(AttributedStyle style) {
        return applyStrategy.applyForForeground(style);
    }
}
