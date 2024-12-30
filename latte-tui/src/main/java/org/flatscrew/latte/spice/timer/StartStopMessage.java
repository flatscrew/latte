package org.flatscrew.latte.spice.timer;

import org.flatscrew.latte.Message;

public record StartStopMessage(
        int id,
        boolean running
) implements Message {
}
