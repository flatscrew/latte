package org.flatscrew.latte.springexample;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.flatscrew.latte.springexample.model.Author;
import org.flatscrew.latte.springexample.model.Book;
import org.flatscrew.latte.springexample.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final Faker faker;
    private final BookRepository bookRepository;

    public long countAllBooks() {
        return bookRepository.count();
    }

    public List<Book> filterByTitle(String title) {
        return bookRepository.findByTitleLike(title);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Transactional
    public void generateBooks(int booksCount) {
        for (int i = 0; i < booksCount; i++) {
            Book book = new Book();
            book.setTitle(faker.book().title());
            book.setSynopsis(String.join(" ", faker.lorem().words(5)));

            // generate authors
            int numAuthors = faker.number().numberBetween(1, 4);
            for (int j = 0; j < numAuthors; j++) {
                Author author = new Author();
                author.setName(faker.name().fullName());
                book.getAuthors().add(author);
            }
            bookRepository.save(book);
        }
    }
}
