package org.flatscrew.latte.examples.listfancy;

import org.flatscrew.latte.spice.list.DefaultItem;

public record FancyItem(String title, String description) implements DefaultItem  {
    @Override
    public String filterValue() {
        return title;
    }
}
