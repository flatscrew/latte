package org.flatscrew.latte.springexample;

import lombok.RequiredArgsConstructor;
import org.flatscrew.latte.spice.list.DefaultItem;
import org.flatscrew.latte.springexample.model.Author;
import org.flatscrew.latte.springexample.model.Book;

import java.util.List;

@RequiredArgsConstructor
public class BookItem implements DefaultItem {

    private final Book book;

    @Override
    public String title() {
        return book.getId() + ". " + book.getTitle();
    }

    public String authors() {
        StringBuilder authorsString = new StringBuilder();

        List<Author> authors = book.getAuthors();
        for (int i = 0; i < authors.size(); i++) {
            authorsString.append("- ").append(authors.get(i).getName());
            if (i + 1 < authors.size()) {
                authorsString.append("\n");
            }
        }

        return authorsString.toString();
    }

    public String getDescription() {
        return book.getDescription();
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
