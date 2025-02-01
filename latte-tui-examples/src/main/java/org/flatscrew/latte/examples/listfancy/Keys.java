package org.flatscrew.latte.examples.listfancy;

import org.flatscrew.latte.spice.key.Binding;

public class Keys {

    private Binding toggleSpinner;
    private Binding toggleTitleBar;
    private Binding toggleStatusBar;
    private Binding togglePagination;
    private Binding toggleHelpMenu;
    private Binding insertItem;

    public Keys() {
        this.insertItem = new Binding(
                Binding.withKeys("a"),
                Binding.withHelp("a", "add item")
        );
        this.toggleSpinner = new Binding(
                Binding.withKeys("s"),
                Binding.withHelp("s", "toggle spinner")
        );
        this.toggleTitleBar = new Binding(
                Binding.withKeys("T"),
                Binding.withHelp("T", "toggle title")
        );
        this.toggleStatusBar = new Binding(
                Binding.withKeys("S"),
                Binding.withHelp("S", "toggle status")
        );
        this.togglePagination = new Binding(
                Binding.withKeys("P"),
                Binding.withHelp("P", "toggle pagination")
        );
        this.toggleHelpMenu = new Binding(
                Binding.withKeys("H"),
                Binding.withHelp("H", "toggle help")
        );
    }

    public Binding toggleSpinner() {
        return toggleSpinner;
    }

    public Binding toggleTitleBar() {
        return toggleTitleBar;
    }

    public Binding toggleStatusBar() {
        return toggleStatusBar;
    }

    public Binding togglePagination() {
        return togglePagination;
    }

    public Binding toggleHelpMenu() {
        return toggleHelpMenu;
    }

    public Binding insertItem() {
        return insertItem;
    }
}
