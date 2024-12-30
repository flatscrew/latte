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

    /**
     * Color creates a {@link TerminalColor} from a string. Valid inputs are hex colors, as well as
     * ANSI color codes (0-15, 16-255).
     */
    public TerminalColor color(String color) {
        if (color == null || color.isBlank()) {
            return null;
        }
        TerminalColor terminalColor;

        if (color.startsWith("#")) {
            terminalColor = new RGBColor(color);
        } else {
            try {
                int colorCode = Integer.parseInt(color);
                if (colorCode < 16) {
                    terminalColor = new ANSIColor(colorCode);
                } else {
                    terminalColor = new ANSI256Color(colorCode);
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return convert(terminalColor);
    }

    /**
     * Transforms given {@link TerminalColor} to a {@link TerminalColor} supported within the {@link ColorProfile}.
     */
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
