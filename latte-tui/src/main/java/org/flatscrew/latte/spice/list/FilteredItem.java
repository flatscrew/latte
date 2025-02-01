package org.flatscrew.latte.spice.list;

public class FilteredItem {
    private int index;
    private Item item;
    private int[] matches;

    public FilteredItem(int index, Item item, int[] matches) {
        this.index = index;
        this.item = item;
        this.matches = matches;
    }

    public FilteredItem(Item item) {
        this.item = item;
        this.matches = new int[0];
    }

    public Item item() {
        return item;
    }

    public int[] matches() {
        return matches;
    }

    public int index() {
        return index;
    }
}