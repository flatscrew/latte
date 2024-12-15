package org.flatscrew.latte.cream;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

public class Style {

    private AttributedStyle style = new AttributedStyle();

    public Style foreground(Color color) {
        this.style = color.applyAsForeground(style);
        return this;
    }

    public Style background(Color color) {
        this.style = color.applyAsBackground(style);
        return this;
    }

    public Style bold() {
        this.style = style.bold();
        return this;
    }

    public String render(String... strings) {
        return new AttributedString(String.join(" ", strings), style).toAnsi();
    }
}
