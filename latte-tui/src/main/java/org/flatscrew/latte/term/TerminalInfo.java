package org.flatscrew.latte.term;

import org.flatscrew.latte.cream.color.TerminalColor;

public record TerminalInfo(boolean tty, TerminalColor backgroundColor) {

    private static TerminalInfoProvider infoProvider;

    public static void provide(TerminalInfoProvider infoProvider) {
        TerminalInfo.infoProvider = infoProvider;
    }

    public static TerminalInfo get() {
        return infoProvider.provide();
    }
}