package org.flatscrew.latte.cream.color;

import org.flatscrew.latte.cream.Renderer;
import org.jline.utils.AttributedStyle;

public class ANSIColor implements TerminalColor, RGBSupplier {

    private final ColorApplyStrategy applyStrategy;
    private final int colorCode;

    // ANSIColor is a color (0-15) as defined by the ANSI Standard.
    public ANSIColor(int colorCode) {
        this.applyStrategy = new ColorCodeApplyStrategy(colorCode);
        this.colorCode = colorCode;
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer) {
        return applyStrategy.applyForBackground(style);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer) {
        return applyStrategy.applyForForeground(style);
    }

    public RGB rgb() {
        return RGB.fromHexString(ANSIColors.ANSI_HEX[colorCode]);
    }
}
