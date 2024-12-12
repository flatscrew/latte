package org.flatscrew.latte.message;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.Message;

public record BatchMessage(Command[] commands) implements Message {
}
