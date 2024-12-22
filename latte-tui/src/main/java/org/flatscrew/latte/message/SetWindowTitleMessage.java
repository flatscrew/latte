package org.flatscrew.latte.message;

import org.flatscrew.latte.Message;

public record SetWindowTitleMessage(String title) implements Message {
}
