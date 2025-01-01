package org.flatscrew.latte.cream.color;

import org.flatscrew.latte.cream.Renderer;
import org.jline.utils.AttributedStyle;

/**
 * RGBColor is a hex-encoded color, e.g. "#ff0000"
 */
public class RGBColor implements TerminalColor {

    private final ColorApplyStrategy colorApplyStrategy;
    private final RGB rgb;

    public RGBColor(String hexValue) {
        this.colorApplyStrategy = new HexColorApplyStrategy(hexValue);
        this.rgb = RGB.fromHexString(hexValue);
    }

    public RGBColor(int r, int g, int b) {
        this.colorApplyStrategy = new RGBAApplyStrategy(r, g, b);
        this.rgb = new RGB(r, g, b);
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer) {
        return colorApplyStrategy.applyForBackground(style);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer) {
        return colorApplyStrategy.applyForForeground(style);
    }

    public RGB rgb() {
        return rgb;
    }

    public ANSI256Color toANSI256Color() {
        return rgb.toANSI256Color();
    }
}
