package org.flatscrew.latte.message;

import org.flatscrew.latte.Message;

public class KeyPressMessage implements Message {
    private final int key;
    private final boolean alt;

    public KeyPressMessage(int key) {
        this(key, false);
    }

    public KeyPressMessage(int key, boolean alt) {
        this.key = key;
        this.alt = alt;
    }

    public int key() { return key; }
    public boolean alt() { return alt; }
}
