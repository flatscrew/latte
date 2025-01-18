package org.flatscrew.latte.spice.paginator;

import org.flatscrew.latte.spice.key.Binding;

public class KeyMap {

    public static KeyMap defaultKeyMap() {
        return new KeyMap(
                new Binding(Binding.withKeys()),
                new Binding(Binding.withKeys())
        );
    }

    private Binding prevPage;
    private Binding nextPage;

    public KeyMap() {
    }

    public KeyMap(Binding prevPage, Binding nextPage) {
        this.prevPage = prevPage;
        this.nextPage = nextPage;
    }

    public Binding nextPage() {
        return nextPage;
    }

    public Binding prevPage() {
        return prevPage;
    }
}
