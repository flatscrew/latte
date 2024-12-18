package org.flatscrew.latte.message;

import org.flatscrew.latte.Message;

public record WindowSizeMessage(int width, int height) implements Message {
}
