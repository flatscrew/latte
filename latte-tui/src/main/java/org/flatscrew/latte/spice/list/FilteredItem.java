package org.flatscrew.latte.spice.list;

public class FilteredItem {
    private int index;
    private Item item;
    private int[] matches;

    public FilteredItem(Item item) {
        this.item = item;
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