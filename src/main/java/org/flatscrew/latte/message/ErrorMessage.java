package org.flatscrew.latte.message;

import org.flatscrew.latte.Message;

public record ErrorMessage(Throwable error) implements Message {
}
