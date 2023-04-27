package com.bookshelf.app.repo


import com.bookshelf.app.model.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(attributePaths = ["publisher"])
    Page<Book> findAll(Pageable pageable)

    @EntityGraph(attributePaths = ["publisher", "authors"])
    Optional<Book> findById(Long id)

    @EntityGraph(attributePaths = ["publisher", "authors"])
    Optional<Book> findBookByTitle(String text)

    @EntityGraph(attributePaths = ["publisher"])
    List<Book> findBooksByTitleContainingIgnoreCase(String text)

    @EntityGraph(attributePaths = ["publisher"])
    Page<Book> findBooksByTitleContainingIgnoreCase(PageRequest paging, String text)
}
