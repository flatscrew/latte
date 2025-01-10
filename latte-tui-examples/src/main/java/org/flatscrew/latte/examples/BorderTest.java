package org.flatscrew.latte.examples;

import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.border.StandardBorder;
import org.flatscrew.latte.cream.color.Color;
import org.flatscrew.latte.cream.color.NoColor;
import org.flatscrew.latte.term.TerminalInfo;

public class BorderTest {

    public static void main(String[] args) {
        TerminalInfo.provide(() -> new TerminalInfo(true, new NoColor()));

        System.out.println();
        System.out.println(
                Style.newStyle()
                        .width(10)
                        .border(StandardBorder.RoundedBorder, true)
                        .borderBackground(Color.color("#ff0000"))
                        .background(Color.color("#ff0000"))
                        .padding(5)
                        .render("This is a test of a frame")
        );
    }
}
