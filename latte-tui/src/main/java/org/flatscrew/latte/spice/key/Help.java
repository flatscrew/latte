package org.flatscrew.latte.spice.key;

public record Help(String key, String desc) {

    public Help() {
        this("", "");
    }
}
