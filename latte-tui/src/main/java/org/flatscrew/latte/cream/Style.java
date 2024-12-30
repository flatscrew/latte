package org.flatscrew.latte.cream;

import org.flatscrew.latte.cream.color.ColorProfile;
import org.flatscrew.latte.cream.color.TerminalColor;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import static org.flatscrew.latte.cream.Renderer.defaultRenderer;

public class Style {

    public static Style newStyle() {
        return defaultRenderer.newStyle();
    }

    private final ColorProfile colorProfile;

    private boolean bold;
    private boolean reverse;
    private TerminalColor background;
    private TerminalColor foreground;

    Style(ColorProfile colorProfile) {
        this.colorProfile = colorProfile;
    }

    public Style foreground(TerminalColor color) {
        this.foreground = color;
        return this;
    }

    public Style background(TerminalColor color) {
        this.background = color;
        return this;
    }

    public Style bold() {
        this.bold = true;
        return this;
    }

    public Style reverse() {
        this.reverse = true;
        return this;
    }

    public String render(String... strings) {
        String str = String.join(" ", strings);

//        TermStyle te = colorProfile.string();

        AttributedStyle style = new AttributedStyle();

        if (foreground != null) {
            style = foreground.applyAsForeground(style);
        }
        if (background != null) {
            style = background.applyAsBackground(style);
        }
        if (bold) {
            style = style.bold();
        }
        if (reverse) {
            style = style.inverse();
        }

        return new AttributedString(String.join(" ", strings), style).toAnsi();
    }
}
