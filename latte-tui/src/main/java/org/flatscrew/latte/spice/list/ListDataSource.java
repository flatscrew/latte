package org.flatscrew.latte.spice.list;

import org.flatscrew.latte.Command;

import java.util.List;

public interface ListDataSource {
    List<? extends Item> fetchItems(int page, int perPage);  // Supports pagination
    long totalItems();
    Command filterItems(String filterValue);
    List<FilteredItem> itemsAsFilteredItems();

    default boolean isEmpty() {
        return totalItems() == 0;
    }
}
