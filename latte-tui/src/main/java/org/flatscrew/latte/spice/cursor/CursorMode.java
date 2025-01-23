package org.flatscrew.latte.spice.cursor;

import java.util.stream.Stream;

public enum CursorMode {
    Blink,
    Static,
    Hide;

    public static CursorMode fromOrdinal(int value) {
        return Stream.of(CursorMode.values()).filter(mode -> mode.ordinal() == value).findAny().orElse(null);
    }
}