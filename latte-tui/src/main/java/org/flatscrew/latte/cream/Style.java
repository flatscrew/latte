package org.flatscrew.latte.cream;

import org.flatscrew.latte.cream.color.ColorProfile;
import org.flatscrew.latte.cream.color.TerminalColor;
import org.jline.utils.AttributedCharSequence;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import static org.flatscrew.latte.cream.Renderer.defaultRenderer;

public class Style {

    public static Style newStyle() {
        return defaultRenderer.newStyle();
    }

    private final Renderer renderer;
    private boolean bold;
    private boolean reverse;
    private TerminalColor background;
    private TerminalColor foreground;

    public Style(Renderer renderer) {
        this.renderer = renderer;
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
        AttributedStyle style = new AttributedStyle();
        if (foreground != null) {
            style = foreground.applyAsForeground(style, renderer);
        }
        if (background != null) {
            style = background.applyAsBackground(style, renderer);
        }
        if (bold) {
            style = style.bold();
        }
        if (reverse) {
            style = style.inverse();
        }

        String string = String.join(" ", strings);
        ColorProfile colorProfile = renderer.colorProfile();
        if (colorProfile == ColorProfile.Ascii) {
            return string;
        }
        return new AttributedString(string, style).toAnsi(colorProfile.colorsCount(), AttributedCharSequence.ForceMode.None);
    }
}
