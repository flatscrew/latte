package org.flatscrew.latte.cream.color;

import org.jline.utils.AttributedStyle;

/**
 * RGBColor is a hex-encoded color, e.g. "#ff0000"
 */
public class RGBColor implements TerminalColor {

    private final HexColorApplyStrategy colorApplyStrategy;
    private final String hexValue;

    public RGBColor(String hexValue) {
        this.colorApplyStrategy = new HexColorApplyStrategy(hexValue);
        this.hexValue = hexValue;
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style) {
        return colorApplyStrategy.applyForBackground(style);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style) {
        return colorApplyStrategy.applyForForeground(style);
    }

    public RGB toRGB() {
        return RGB.fromHexString(hexValue);
    }
}
