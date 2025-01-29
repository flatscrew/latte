package org.flatscrew.latte.spice.list;

import org.flatscrew.latte.spice.key.Binding;

public class Keys {

    private final Binding cursorUp;
    private final Binding cursorDown;
    private final Binding nextPage;
    private final Binding prevPage;
    private final Binding goToStart;
    private final Binding goToEnd;
    private final Binding filter;
    private final Binding clearFilter;
    private final Binding cancelWhileFiltering;
    private final Binding acceptWhileFiltering;
    private final Binding showFullHelp;
    private final Binding closeFullHelp;
    private final Binding quit;
    private final Binding forceQuit;

    public Keys() {
        // Browsing
        this.cursorUp = new Binding(Binding.withKeys("up", "k"));
        this.cursorDown = new Binding(Binding.withKeys("down", "j"));
        this.prevPage = new Binding(Binding.withKeys("left", "h", "pgup", "b", "u"));
        this.nextPage = new Binding(Binding.withKeys("right", "l", "pgdown", "f", "d"));
        this.goToStart = new Binding(Binding.withKeys("home", "g"));
        this.goToEnd = new Binding(Binding.withKeys("end", "G"));

        // Filtering
        this.filter = new Binding(Binding.withKeys("/"));
        this.clearFilter = new Binding(Binding.withKeys("esc"));
        this.cancelWhileFiltering = new Binding(Binding.withKeys("esc"));
        this.acceptWhileFiltering = new Binding(Binding.withKeys("enter", "tab", "shift+tab", "ctrl+k", "up", "ctrl+j", "down"));

        // Help
        this.showFullHelp = new Binding(Binding.withKeys("?"));
        this.closeFullHelp = new Binding(Binding.withKeys("?"));

        // Quitting
        this.quit = new Binding(Binding.withKeys("q", "esc"));
        this.forceQuit = new Binding(Binding.withKeys("ctrl+c"));
    }

    public Binding cursorUp() {
        return cursorUp;
    }

    public Binding cursorDown() {
        return cursorDown;
    }

    public Binding nextPage() {
        return nextPage;
    }

    public Binding prevPage() {
        return prevPage;
    }

    public Binding goToStart() {
        return goToStart;
    }

    public Binding goToEnd() {
        return goToEnd;
    }

    public Binding filter() {
        return filter;
    }

    public Binding clearFilter() {
        return clearFilter;
    }

    public Binding cancelWhileFiltering() {
        return cancelWhileFiltering;
    }

    public Binding acceptWhileFiltering() {
        return acceptWhileFiltering;
    }

    public Binding showFullHelp() {
        return showFullHelp;
    }

    public Binding closeFullHelp() {
        return closeFullHelp;
    }

    public Binding quit() {
        return quit;
    }

    public Binding forceQuit() {
        return forceQuit;
    }
}
