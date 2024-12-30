package org.flatscrew.latte.cream.term;

import org.flatscrew.latte.cream.color.ColorProfile;

import static java.lang.System.getenv;

public class Output {

    private static Output defaultOutput = new Output();

    public static Output defaultOutput() {
        return defaultOutput;
    }

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
        // TODO
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

}
