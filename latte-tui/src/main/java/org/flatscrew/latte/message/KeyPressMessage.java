package org.flatscrew.latte.message;

import org.flatscrew.latte.Message;
import org.flatscrew.latte.input.key.Key;
import org.flatscrew.latte.input.key.KeyType;

public class KeyPressMessage implements Message {

    private final Key key;

    public KeyPressMessage(Key key) {
        this.key = key;
    }

    public String key() {
        return key.toString();
    }

    public char[] runes() {
        return key.runes();
    }

    public KeyType type() {
        return key.type();
    }

    public boolean alt() {
        return key.alt();
    }
}
