package org.flatscrew.latte.examples;

import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.border.StandardBorder;
import org.flatscrew.latte.cream.color.Color;
import org.flatscrew.latte.cream.color.NoColor;
import org.flatscrew.latte.term.TerminalInfo;

public class BorderTest {

    public static void main(String[] args) {
        TerminalInfo.provide(() -> new TerminalInfo(true, new NoColor()));

        System.out.println(
                Style.newStyle()
                        .width(10)
                        .padding(5)
                        .margin(3)
                        .border(StandardBorder.RoundedBorder)
                        .borderBackground(Color.color("#ff0000"))
                        .background(Color.color("#ff0000"))
                        .render("This is a test of a frame")
        );
    }
}
