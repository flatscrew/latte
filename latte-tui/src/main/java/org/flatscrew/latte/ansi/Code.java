package org.flatscrew.latte.ansi;

public enum Code {

    EnableFocusReporting("\u001b[?1004h"),
    DisableFocusReporting("\u001b[?1004l"),
    EnableMouseAllMotion("\u001b[?1003h"),
    DisableMouseAllMotion("\u001b[?1003l"),
    EnableMouseSgrExt("\u001b[?1006h"),
    DisableMouseSgrExt("\u001b[?1006l");

    private final String value;

    Code(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
