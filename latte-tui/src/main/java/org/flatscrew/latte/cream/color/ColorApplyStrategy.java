package org.flatscrew.latte.cream.color;

import org.jline.utils.AttributedStyle;

public interface ColorApplyStrategy {
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

class HexColorApplyStrategy implements ColorApplyStrategy {

    private final ColorApplyStrategy rgbaApplyStrategy;

    public HexColorApplyStrategy(String hexValue) {
        String hex = hexValue.replace("#", "").trim();
        this.rgbaApplyStrategy = new RGBAApplyStrategy(
                Integer.parseInt(hex.substring(0, 2), 16),
                Integer.parseInt(hex.substring(2, 4), 16),
                Integer.parseInt(hex.substring(4, 6), 16)
        );
    }

    @Override
    public AttributedStyle applyForForeground(AttributedStyle attributedStyle) {
        return rgbaApplyStrategy.applyForForeground(attributedStyle);
    }

    @Override
    public AttributedStyle applyForBackground(AttributedStyle attributedStyle) {
        return rgbaApplyStrategy.applyForBackground(attributedStyle);
    }
}