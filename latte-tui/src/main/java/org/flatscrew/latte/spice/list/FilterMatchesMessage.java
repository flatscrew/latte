package org.flatscrew.latte.spice.list;

import org.flatscrew.latte.Message;

public record FilterMatchesMessage(java.util.List<FilteredItem> filteredItems) implements Message {
}
