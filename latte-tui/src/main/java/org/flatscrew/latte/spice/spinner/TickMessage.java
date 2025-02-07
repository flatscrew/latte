package org.flatscrew.latte.spice.spinner;

import org.flatscrew.latte.Message;

import java.time.LocalDateTime;
import java.util.Objects;

public final class TickMessage implements Message {
    private final LocalDateTime time;
    private final int tag;
    private final int id;

    public TickMessage(
            LocalDateTime time,
            int tag,
            int id) {
        this.time = time;
        this.tag = tag;
        this.id = id;
    }

    public LocalDateTime time() {
        return time;
    }

    public int tag() {
        return tag;
    }

    public int id() {
        return id;
    }

}