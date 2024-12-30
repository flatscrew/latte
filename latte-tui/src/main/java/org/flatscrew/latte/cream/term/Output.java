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
        // TODO
        return ColorProfile.ANSI;
    }

    private boolean envNoColor() {
        return !getenv("NO_COLOR").isBlank() || (getenv("CLICOLOR").equals("0") && !cliColorForced());
    }

    private boolean cliColorForced() {
        String force = getenv("CLICOLOR_FORCE");
        if (force == null || force.isBlank()) {
            return false;
        }
        return !"0".equals(force);
    }
}
