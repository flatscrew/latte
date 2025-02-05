package org.flatscrew.latte.springexample.repository;

import org.flatscrew.latte.springexample.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(name = "Book.findByTitleIsContaining")
    Page<Book> findByTitleIsContaining(@Param("title") String title, Pageable pageable);
}
