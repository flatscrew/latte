package org.flatscrew.latte.spice.paginator;

import org.flatscrew.latte.spice.key.Binding;

public class KeyMap {

    public static KeyMap defaultKeyMap() {
        return new KeyMap(
                new Binding(Binding.withKeys(68, 53, 'h')),
                new Binding(Binding.withKeys(67, 54, 'l'))
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
