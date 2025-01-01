package org.flatscrew.latte.term;

import org.flatscrew.latte.cream.color.TerminalColor;

public record TerminalInfo(boolean tty, TerminalColor backgroundColor) {

    private static TerminalInfo terminalInfo;

    public static void provide(TerminalInfoProvider infoProvider) {
        terminalInfo = infoProvider.provide();
    }

    public static TerminalInfo get() {
        return terminalInfo;
    }
}