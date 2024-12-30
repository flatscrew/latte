package org.flatscrew.latte.cream.color;

import org.jline.utils.AttributedStyle;

public interface TerminalColor {
    AttributedStyle applyAsBackground(AttributedStyle style);
    AttributedStyle applyAsForeground(AttributedStyle style);
}
