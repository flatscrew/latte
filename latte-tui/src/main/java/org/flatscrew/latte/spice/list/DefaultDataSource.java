package org.flatscrew.latte.spice.list;

import org.flatscrew.latte.Command;
import org.flatscrew.latte.spice.list.fuzzy.FuzzyFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class DefaultDataSource implements ListDataSource {

    private final List list;
    private FilterFunction filterFunction;
    private java.util.List<Item> items;

    public DefaultDataSource(List list, Item... items) {
        this.list = list;
        this.items = new ArrayList<>(Arrays.asList(items));
        this.filterFunction = FuzzyFilter::defaultFilter;
    }

    public Command setItem(int index, Item item) {
        Command cmd = null;
        this.items.set(index, item);

        if (list.filterState() != FilterState.Unfiltered) {
//            cmd = filterItems("");
        }

        list.updatePagination();
        return cmd;
    }

    public Command insertItem(int index, Item item) {
        Command cmd = null;
        this.items.add(index, item);

        if (list.filterState() != FilterState.Unfiltered) {
//            cmd = filterItems("");
        }

        list.updatePagination();
        list.updateKeybindings();
        return cmd;
    }

    public void removeItem(int index) {
        this.items.remove(index);

        if (list.filterState() != FilterState.Unfiltered) {
//            this.matchedItems.remove(index);
//
//            if (matchedItems.isEmpty()) {
//                list.resetFiltering();
//            }
        }
        list.updatePagination();
    }

    public java.util.List<Item> items() {
        return items;
    }

    public Command setItems(Item... items) {
        this.items = java.util.List.of(items);
        Command cmd = null;

//        if (filterState != FilterState.Unfiltered) {
//            this.matchedItems = null;
//            cmd = filterItems("");
//        }

        list.updatePagination();
        list.updateKeybindings();
        return cmd;
    }

    public boolean isEmpty() {
        return items == null || items().isEmpty();
    }

    @Override
    public FetchedItems fetchItems(int page, int perPage, String filterValue) {
        java.util.List<FilteredItem> filteredItems;

        if (filterValue == null || filterValue.isEmpty()) {
            filteredItems = allItemsAsFilteredItems();
        } else {
            String[] targets = new String[items.size()];
            for (int i = 0; i < items.size(); i++) {
                targets[i] = items.get(i).filterValue();
            }

            java.util.List<FilteredItem> filterMatches = new LinkedList<>();
            Rank[] ranks = filterFunction.apply(filterValue, targets);
            for (Rank rank : ranks) {
                filterMatches.add(new FilteredItem(
                        rank.getIndex(),
                        items.get(rank.getIndex()),
                        rank.getMatchedIndexes()
                ));
            }

            filteredItems = filterMatches;
        }

        int offset = page * perPage;
        int toIndex = Math.min(offset + perPage, filteredItems.size());

        if (offset >= filteredItems.size()) {
            return new FetchedItems();
        }

        return new FetchedItems(filteredItems.subList(offset, toIndex), filteredItems.size(), items.size());
    }

    private java.util.List<FilteredItem> allItemsAsFilteredItems() {
        return items.stream().map(FilteredItem::new).toList();
    }
}
