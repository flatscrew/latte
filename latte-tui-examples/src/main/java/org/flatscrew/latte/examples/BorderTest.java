package org.flatscrew.latte.examples;

import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.border.StandardBorder;
import org.flatscrew.latte.cream.color.NoColor;
import org.flatscrew.latte.term.TerminalInfo;
import org.flatscrew.latte.term.TerminalInfoProvider;

public class BorderTest {

    public static void main(String[] args) {
        TerminalInfo.provide(new TerminalInfoProvider() {
            @Override
            public TerminalInfo provide() {
                return new TerminalInfo(true, new NoColor());
            }
        });

        System.out.println();
        System.out.println(
                Style.newStyle().border(StandardBorder.DoubleBorder, true, false).render("This is a test of a frame")
        );
    }
}
