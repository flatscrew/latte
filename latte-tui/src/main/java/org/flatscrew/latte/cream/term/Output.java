package org.flatscrew.latte.cream.term;

import org.flatscrew.latte.cream.color.ANSI256Color;
import org.flatscrew.latte.cream.color.ANSIColor;
import org.flatscrew.latte.cream.color.ColorProfile;
import org.flatscrew.latte.cream.color.HSL;
import org.flatscrew.latte.cream.color.NoColor;
import org.flatscrew.latte.cream.color.RGB;
import org.flatscrew.latte.cream.color.RGBColor;
import org.flatscrew.latte.cream.color.TerminalColor;
import org.flatscrew.latte.term.TerminalInfo;

import java.util.Optional;

import static java.lang.System.getenv;
import static java.util.Optional.ofNullable;

public class Output {

    private static Output defaultOutput = new Output();

    public static Output defaultOutput() {
        return defaultOutput;
    }

    private TerminalColor backgroundColor = new NoColor();

    public ColorProfile envColorProfile() {
        if (envNoColor()) {
            return ColorProfile.Ascii;
        }
        ColorProfile colorProfile = colorProfile();
        if (cliColorForced() && colorProfile == ColorProfile.Ascii) {
            return ColorProfile.ANSI;
        }
        return colorProfile;
    }

    private ColorProfile colorProfile() {
        if (!TerminalInfo.get().tty()) {
            return ColorProfile.Ascii;
        }
        if ("true".equals(getenv("GOOGLE_CLOUD_SHELL"))) {
            return ColorProfile.TrueColor;
        }

        String term = getenv("TERM");
        String colorTerm = ofNullable(getenv("COLORTERM")).orElse("");

        switch (colorTerm.toLowerCase()) {
            case "24bit":
            case "truecolor":
                if (term.startsWith("screen")) {
                    if (!"tmux".equals(getenv("TERM_PROGRAM"))) {
                        return ColorProfile.ANSI256;
                    }
                }
                return ColorProfile.TrueColor;
            case "yes":
            case "true":
                return ColorProfile.ANSI256;
        }

        switch (term) {
            case "xterm-kity":
            case "wezterm":
                return ColorProfile.TrueColor;
            case "linux":
                return ColorProfile.ANSI;
        }

        if (term.contains("256color")) return ColorProfile.ANSI256;
        if (term.contains("color")) return ColorProfile.ANSI;
        if (term.contains("ansi")) return ColorProfile.ANSI;

        return ColorProfile.ANSI256;
    }

    private boolean envNoColor() {
        String noColor = getenv("NO_COLOR");
        String clicolor = getenv("CLICOLOR");

        return (noColor != null && !noColor.isBlank()) || ((clicolor != null && clicolor.equals("0")) && !cliColorForced());
    }

    private boolean cliColorForced() {
        String force = getenv("CLICOLOR_FORCE");
        if (force == null || force.isBlank()) {
            return false;
        }
        return !"0".equals(force);
    }

    public boolean hasDarkBackground() {
        TerminalColor terminalColor = backgroundColor();

        RGB rgb = RGB.black();
        if (terminalColor instanceof RGBColor rgbColor) {
            rgb = rgbColor.rgb();
        } else if (terminalColor instanceof ANSIColor ansiColor) {
            rgb = ansiColor.rgb();
        } else if (terminalColor instanceof ANSI256Color ansi256Color) {
            rgb = ansi256Color.rgb();
        }

        HSL hsl = rgb.toHSL();
        return hsl.isDark();
    }

    public TerminalColor backgroundColor() {
        if (!TerminalInfo.get().tty()) {
            return backgroundColor;
        }

        TerminalColor terminalColor = TerminalInfo.get().backgroundColor();
        if (terminalColor == null) {
            String colorfgbg = Optional.ofNullable(getenv("COLORFGBG")).orElse("");
            if (colorfgbg.contains(";")) {
                String[] strings = colorfgbg.split(";");
                try {
                    int colorCode = Integer.parseInt(strings[strings.length-1]);
                    return new ANSIColor(colorCode);
                } catch (NumberFormatException e) {
                    // do nothing
                }
            }
        }
        return new NoColor();
    }
}