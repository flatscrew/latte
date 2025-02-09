package org.flatscrew.latte.spice.list;

public interface ListDataSource {
    FetchedItems fetchItems(int page, int perPage, String filterValue);
}
