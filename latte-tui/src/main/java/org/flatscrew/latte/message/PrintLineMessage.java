package org.flatscrew.latte.message;

import org.flatscrew.latte.Message;

public record PrintLineMessage(String messageBody) implements Message {
}
