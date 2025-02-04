package org.flatscrew.latte.springexample.repository;

import org.flatscrew.latte.springexample.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(name = "Book.findByTitleLike")
    List<Book> findByTitleLike(@Param("title") String title);
}
