package org.flatscrew.latte.springexample;

import lombok.RequiredArgsConstructor;
import org.flatscrew.latte.spice.list.DefaultItem;
import org.flatscrew.latte.springexample.model.Book;

@RequiredArgsConstructor
public class BookItem implements DefaultItem {

    private final Book book;

    @Override
    public String title() {
        return book.getId() + ". " + book.getTitle();
    }

    @Override
    public String description() {
        return book.getSynopsis();
    }

    @Override
    public String filterValue() {
        return book.getTitle();
    }
}
