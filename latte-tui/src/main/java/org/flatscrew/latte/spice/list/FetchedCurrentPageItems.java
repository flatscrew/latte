package org.flatscrew.latte.spice.list;

import org.flatscrew.latte.Message;

public record FetchedCurrentPageItems(java.util.List<? extends Item> items, Runnable... postFetch) implements Message {
}
