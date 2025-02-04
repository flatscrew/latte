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
            cmd = filterItems("");
        }

        list.updatePagination();
        return cmd;
    }

    public Command insertItem(int index, Item item) {
        Command cmd = null;
        this.items.add(index, item);

        if (list.filterState() != FilterState.Unfiltered) {
            cmd = filterItems("");
        }

        list.updatePagination();
        list.updateKeybindings();
        return cmd;
    }

    public void removeItem(int index) {
        this.items.remove(index);

        if (list.filterState() != FilterState.Unfiltered) {
//            this.filteredItems.remove(index);
//
//            if (filteredItems.isEmpty()) {
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
//            this.filteredItems = null;
//            cmd = filterItems("");
//        }

        list.updatePagination();
        list.updateKeybindings();
        return cmd;
    }

    @Override
    public boolean isEmpty() {
        return items == null || items().isEmpty();
    }

    @Override
    public java.util.List<? extends Item> fetchItems(int page, int perPage) {
        int offset = page * perPage;
        int toIndex = Math.min(offset + perPage, items.size());

        if (offset >= items.size()) {
            return java.util.List.of();
        }

        return items.subList(offset, toIndex); // Re
    }

    @Override
    public long totalItems() {
        return items.size();
    }

    @Override
    public Command filterItems(String filterValue) {
        return () -> {
            if ("".equals(filterValue)) {
                return new FilterMatchesMessage(itemsAsFilteredItems());
            }

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
            return new FilterMatchesMessage(filterMatches);
        };
    }

    @Override
    public java.util.List<FilteredItem> itemsAsFilteredItems() {
        return items.stream().map(FilteredItem::new).toList();
    }
}
