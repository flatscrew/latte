package org.flatscrew.latte.cream.color;

import org.jline.utils.AttributedStyle;

public class ANSI256Color implements TerminalColor {

    private final ColorCodeApplyStrategy applyStrategy;
    private final int colorCode;

    public ANSI256Color(int colorCode) {
        this.applyStrategy = new ColorCodeApplyStrategy(colorCode);
        this.colorCode = colorCode;
    }

    @Override
    public AttributedStyle applyAsBackground(AttributedStyle style) {
        return applyStrategy.applyForBackground(style);
    }

    @Override
    public AttributedStyle applyAsForeground(AttributedStyle style) {
        return applyStrategy.applyForForeground(style);
    }

    public ANSIColor toANSIColor() {
        int ansiColorCode = 0;
        float minDistance = Float.MAX_VALUE;
        RGB rgb = RGB.fromHexString(ANSIColors.ANSI_HEX[colorCode]);

        for (int colorIndex = 0; colorIndex <= 15; colorIndex++) {
            RGB candidate = RGB.fromHexString(ANSIColors.ANSI_HEX[colorIndex]);
            float distance = rgb.distanceHSLuv(candidate);

            if (distance < minDistance) {
                minDistance = distance;
                ansiColorCode = colorIndex;
            }
        }
        return new ANSIColor(ansiColorCode);
    }
}