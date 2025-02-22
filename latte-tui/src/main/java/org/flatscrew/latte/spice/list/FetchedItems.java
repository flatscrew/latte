package org.flatscrew.latte.spice.list;

import java.util.List;

public record FetchedItems(List<FilteredItem> items, long matchedItems, long totalItems, int totalPages) {

    public FetchedItems() {
        this(List.of(), 0, 0, 0);
    }
}
