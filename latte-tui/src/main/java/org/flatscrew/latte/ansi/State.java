package org.flatscrew.latte.ansi;

public enum State {
    GROUND,
    CSI_ENTRY,
    CSI_INTERMEDIATE,
    CSI_PARAM,
    DCS_ENTRY,
    DCS_INTERMEDIATE,
    DCS_PARAM,
    DCS_STRING,
    ESCAPE,
    ESCAPE_INTERMEDIATE,
    OSC_STRING,
    SOS_STRING,
    PM_STRING,
    APC_STRING,
    UTF8;

    public static State fromOrdinal(int ordinal) {
        return values()[ordinal];
    }
}
