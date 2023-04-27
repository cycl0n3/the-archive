package com.bookshelf.app.repo

import com.bookshelf.app.model.Author
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorRepository extends JpaRepository<Author, Long> {

    Page<Author> findAuthorsByNameContainingIgnoreCase(PageRequest pageRequest, String text)

    Optional<Author> findAuthorByName(String s)
}
