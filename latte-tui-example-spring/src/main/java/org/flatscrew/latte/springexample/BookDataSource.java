package org.flatscrew.latte.springexample;

import lombok.RequiredArgsConstructor;
import org.flatscrew.latte.Command;
import org.flatscrew.latte.spice.list.FilterMatchesMessage;
import org.flatscrew.latte.spice.list.FilteredItem;
import org.flatscrew.latte.spice.list.Item;
import org.flatscrew.latte.spice.list.ListDataSource;
import org.flatscrew.latte.springexample.model.Book;
import org.flatscrew.latte.springexample.repository.BookRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookDataSource implements ListDataSource {

    private final BookRepository bookRepository;

    private List<BookItem> visibleItems = new LinkedList<>();

    @Override
    public List<? extends Item> fetchItems(int offset, int limit) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return bookRepository
                .findAll(PageRequest.of(offset, limit))
                .stream()
                .map(BookItem::new)
                .toList();
    }

    @Override
    public long totalItems() {
        return bookRepository.count();
    }

    @Override
    public Command filterItems(String filterValue) {
        return () -> {
            List<Book> books;
            if (filterValue.isEmpty()) {
                books = bookRepository.findAll();
            } else {
                books = bookRepository.findByTitleLike(filterValue);
            }

            this.visibleItems = books.stream()
                    .map(BookItem::new)
                    .toList();

            return new FilterMatchesMessage(
                    visibleItems
                            .stream()
                            .map(FilteredItem::new)
                            .toList()
            );
        };
    }

    @Override
    public List<FilteredItem> itemsAsFilteredItems() {
        return List.of();
    }
}
