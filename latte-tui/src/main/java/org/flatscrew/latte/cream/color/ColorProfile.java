package org.flatscrew.latte.cream.color;

public enum ColorProfile {
    // TrueColor, 24-bit color profile
    TrueColor,
    // ANSI256, 8-bit color profile
    ANSI256,
    // ANSI, 4-bit color profile
    ANSI,
    // Ascii, uncolored profile
    Ascii;

    // Transforms given TerminalColor
    public TerminalColor convert(TerminalColor terminalColor) {
        if (this == Ascii) {
            return new NoColor();
        }

        if (terminalColor instanceof ANSIColor) {
            return terminalColor;
        } else if (terminalColor instanceof ANSI256Color ansi256Color) {
            if (this == ANSI) {
                return ansi256Color.toANSIColor();
            }
            return terminalColor;
        } else if (terminalColor instanceof RGBColor rgbColor) {
            if (this != TrueColor) {
                RGB rgb = rgbColor.toRGB();
                ANSI256Color ansi256Color = rgb.toANSI256Color();
                if (this == ANSI) {
                    return ansi256Color.toANSIColor();
                }
                return ansi256Color;
            }
            return terminalColor;
        }
        return terminalColor;
    }
}
