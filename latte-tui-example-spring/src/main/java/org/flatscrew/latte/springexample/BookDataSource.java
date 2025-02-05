package org.flatscrew.latte.springexample;

import lombok.RequiredArgsConstructor;
import org.flatscrew.latte.spice.list.FetchedItems;
import org.flatscrew.latte.spice.list.FilteredItem;
import org.flatscrew.latte.spice.list.ListDataSource;
import org.flatscrew.latte.springexample.model.Book;
import org.flatscrew.latte.springexample.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookDataSource implements ListDataSource {

    private final BookRepository bookRepository;

    @Override
    public FetchedItems fetchItems(int offset, int limit, String filterValue) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long allItems = bookRepository.count();

        PageRequest pageRequest = PageRequest.of(offset, limit);
        if (filterValue == null || filterValue.length() < 3) {
            return fetchedItems(
                    bookRepository.findAll(pageRequest),
                    allItems,
                    filterValue
            );
        }

        return fetchedItems(
                bookRepository.findByTitleIsContaining(filterValue, pageRequest),
                allItems,
                filterValue);
    }

    private static FetchedItems fetchedItems(Page<Book> filteredBooks, long allItems, String filterValue) {
        return new FetchedItems(
                filteredBooks
                        .stream()
                        .map(BookItem::new)
                        .map(item -> new FilteredItem(0, item, findMatchedIndexes(item.title(), filterValue)))
                        .toList(),
                filteredBooks.getTotalElements(),
                allItems
        );
    }

    private static int[] findMatchedIndexes(String title, String filterValue) {
        if (filterValue == null || filterValue.isEmpty()) {
            return new int[0];
        }

        List<Integer> indexes = new ArrayList<>();
        int index = title.indexOf(filterValue);

        while (index != -1) {
            // Add all characters of the match to the list
            for (int i = 0; i < filterValue.length(); i++) {
                indexes.add(index + i);
            }
            index = title.indexOf(filterValue, index + 1); // Find next occurrence
        }

        return indexes.stream().mapToInt(Integer::intValue).toArray();
    }
}
