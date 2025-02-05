package org.flatscrew.latte.spice.list;

import org.flatscrew.latte.Message;

public record FetchedCurrentPageItems(
        FetchedItems fetchedItems,
        Runnable... postFetch
) implements Message {
}
