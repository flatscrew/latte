package org.flatscrew.latte.message;

import org.flatscrew.latte.Message;

public class UnknownSequenceMessage implements Message {
    private final String sequence;

    public UnknownSequenceMessage(String sequence) {
        this.sequence = sequence;
    }

    public String sequence() { return sequence; }
}