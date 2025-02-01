package org.flatscrew.latte.examples.listfancy;

import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.color.AdaptiveColor;
import org.flatscrew.latte.cream.color.Color;

import java.util.function.Function;

public class Styles {

    public static Style appStyle = Style.newStyle().padding(1, 2);

    public static Style titleStyle = Style.newStyle()
            .foreground(Color.color("#FFFDF5"))
            .background(Color.color("#25A065"))
            .padding(0, 1);

    public static Function<String[], String> statusMessageStyle = Style.newStyle()
            .foreground(new AdaptiveColor("#04B575", "#04B575"))::render;
}
