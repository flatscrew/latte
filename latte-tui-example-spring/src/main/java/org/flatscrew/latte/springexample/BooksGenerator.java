package org.flatscrew.latte.springexample;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.flatscrew.latte.springexample.model.Author;
import org.flatscrew.latte.springexample.model.Book;
import org.flatscrew.latte.springexample.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BooksGenerator {

    private final Faker faker;
    private final BookRepository bookRepository;

    @Transactional
    public void generateBooks(int booksCount) {
        for (int i = 0; i < booksCount; i++) {
            Book book = new Book();
            book.setTitle(faker.book().title());
            book.setSynopsis(String.join(" ", faker.lorem().sentence(3)));

            // generate authors
            int numAuthors = faker.number().numberBetween(1, 4);
            for (int j = 0; j < numAuthors; j++) {
                Author author = new Author();
                author.setName(faker.name().fullName());
                book.getAuthors().add(author);
            }

            book.setDescription(String.join("\n", faker.lorem().paragraphs(5)));
            bookRepository.save(book);
        }
    }
}
