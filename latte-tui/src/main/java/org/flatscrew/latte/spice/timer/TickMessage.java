package org.flatscrew.latte.spice.timer;

import org.flatscrew.latte.Message;

public record TickMessage(
        int id,
        boolean timeout,
        int tag
) implements Message {
}
