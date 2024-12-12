package org.flatscrew.latte.spice.spinner;

import org.flatscrew.latte.Message;

import java.time.LocalDateTime;

public record TickMessage(
        LocalDateTime time,
        int tag,
        int id) implements Message {
}