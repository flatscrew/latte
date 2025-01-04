package org.flatscrew.latte.ansi;

public enum Action {
    NONE,
    CLEAR,
    COLLECT,
    MARKER,
    DISPATCH,
    EXECUTE,
    START,
    PUT,
    PARAM,
    PRINT,
    IGNORE;

    public static Action fromOrdinal(int ordinal) {
        return values()[ordinal];
    }
}