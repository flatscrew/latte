package org.flatscrew.latte.cream;

import org.flatscrew.latte.ansi.TextWrapper;
import org.flatscrew.latte.cream.color.ColorProfile;
import org.flatscrew.latte.cream.color.TerminalColor;
import org.flatscrew.latte.cream.margin.MarginDecorator;
import org.flatscrew.latte.cream.padding.PaddingDecorator;
import org.jline.utils.AttributedCharSequence.ForceMode;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;

import java.util.Arrays;

import static org.flatscrew.latte.cream.Renderer.defaultRenderer;

public class Style {

    public static Style newStyle() {
        return defaultRenderer.newStyle();
    }

    private final Renderer renderer;

    private TerminalColor background;
    private TerminalColor foreground;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean blink;
    private boolean faint;
    private boolean reverse;
    private boolean inline;
    private int width;

    private int topPadding;
    private int rightPadding;
    private int bottomPadding;
    private int leftPadding;

    private int topMargin;
    private int rightMargin;
    private int bottomMargin;
    private int leftMargin;

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

    public Style italic() {
        this.italic = true;
        return this;
    }

    public Style underline() {
        this.underline = true;
        return this;
    }

    public Style reverse() {
        this.reverse = true;
        return this;
    }

    public Style blink() {
        this.blink = true;
        return this;
    }

    public Style faint() {
        this.faint = true;
        return this;
    }

    public Style inline() {
        this.inline = true;
        return this;
    }

    public Style width(int width) {
        this.width = width;
        return this;
    }

    public Style padding(int... values) {
        int[] boxValues = expandBoxValues(values);
        this.topPadding = boxValues[0];
        this.rightPadding = boxValues[1];
        this.bottomPadding = boxValues[2];
        this.leftPadding = boxValues[3];
        return this;
    }

    public Style paddingTop(int topPadding) {
        this.topPadding = topPadding;
        return this;
    }

    public Style paddingRight(int rightPadding) {
        this.rightPadding = rightPadding;
        return this;
    }

    public Style paddingBottom(int bottomPadding) {
        this.bottomPadding = bottomPadding;
        return this;
    }

    public Style paddingLeft(int leftPadding) {
        this.leftPadding = leftPadding;
        return this;
    }

    public Style margin(int... values) {
        int[] boxValues = expandBoxValues(values);
        this.topMargin = boxValues[0];
        this.rightMargin = boxValues[1];
        this.bottomMargin = boxValues[2];
        this.leftMargin = boxValues[3];
        return this;
    }

    public Style marginTop(int topMargin) {
        this.topMargin = topMargin;
        return this;
    }

    public Style marginRight(int rightMargin) {
        this.rightMargin = rightMargin;
        return this;
    }
    public Style marginBottom(int bottomMargin) {
        this.bottomMargin = bottomMargin;
        return this;
    }

    public Style marginLeft(int leftMargin) {
        this.leftMargin = leftMargin;
        return this;
    }

    // TODO

    public Style marginBackgroundColor(TerminalColor marginBackgroundColor) {
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
        if (italic) {
            style = style.italic();
        }
        if (underline) {
            style = style.underline();
        }
        if (faint) {
            style = style.faint();
        }
        if (blink) {
            style = style.blink();
        }
        if (reverse) {
            style = style.inverse();
        }

        String string = String.join(" ", strings);

        if (!inline && width > 0) {
            int wrapAt = width - leftPadding - rightPadding;
            string = new TextWrapper().wrap(string, wrapAt);
        }

        ColorProfile colorProfile = renderer.colorProfile();
        if (colorProfile != ColorProfile.Ascii) {
            string = new AttributedString(string, style).toAnsi(colorProfile.colorsCount(), ForceMode.None);
        }

        if (!inline) {
            string = PaddingDecorator.applyPadding(string, topPadding, rightPadding, bottomPadding, leftPadding);
        }

        // TODO apply alignment

        if (!inline) {
            string = MarginDecorator.applyMargins(string, topMargin, rightMargin, bottomMargin, leftMargin);
        }
        return string;
    }

    public static int[] expandBoxValues(int... values) {
        int[] result = new int[4];  // top, right, bottom, left

        switch (values.length) {
            case 1:
                Arrays.fill(result, values[0]);
                break;
            case 2:
                result[0] = values[0];  // top
                result[1] = values[1];  // right
                result[2] = values[0];  // bottom
                result[3] = values[1];  // left
                break;
            case 3:
                result[0] = values[0];  // top
                result[1] = values[1];  // right
                result[2] = values[2];  // bottom
                result[3] = values[1];  // left
                break;
            case 4:
                System.arraycopy(values, 0, result, 0, 4);
                break;
            default:
                throw new IllegalArgumentException("Expected 1-4 values, got " + values.length);
        }
        return result;
    }
}
