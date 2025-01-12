package org.flatscrew.latte.cream.color;

import org.flatscrew.latte.cream.Renderer;
import org.jline.utils.AttributedStyle;

/**
 * RGBColor is a hex-encoded color, e.g. "#ff0000"
 */
public final class RGBColor implements TerminalColor, RGBSupplier {

    private final ColorApplyStrategy colorApplyStrategy;
    private final RGB rgb;

    public RGBColor(String hexValue) {
        this.colorApplyStrategy = new HexColorApplyStrategy(hexValue);
        this.rgb = RGB.fromHexString(hexValue);
    }

    public RGBColor(int r, int g, int b) {
        this.rgb = new RGB(r, g, b);
        this.colorApplyStrategy = rgb().asColorApplyStrategy();
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer) {
        return colorApplyStrategy.applyForBackground(style);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer) {
        return colorApplyStrategy.applyForForeground(style);
    }

    @Override
    public RGB rgb() {
        return rgb;
    }

    public ANSI256Color toANSI256Color() {
        return rgb.toANSI256Color();
    }
}
