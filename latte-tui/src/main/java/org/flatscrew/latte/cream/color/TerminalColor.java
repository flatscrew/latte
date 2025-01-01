package org.flatscrew.latte.cream.color;

import org.flatscrew.latte.cream.Renderer;
import org.jline.utils.AttributedStyle;

public interface TerminalColor {
    AttributedStyle applyAsBackground(AttributedStyle style, Renderer renderer);
    AttributedStyle applyAsForeground(AttributedStyle style, Renderer renderer);
}
