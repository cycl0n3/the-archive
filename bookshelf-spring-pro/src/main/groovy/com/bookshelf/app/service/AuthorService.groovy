package com.bookshelf.app.service

import com.bookshelf.app.model.Author
import com.bookshelf.app.repo.AuthorRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service("authorService")
class AuthorService extends SimpleService<Author, Long> {

    @Autowired
    AuthorRepository authorRepository

    @Override
    JpaRepository<Author, Long> getRepository() {
        authorRepository
    }

    Optional<Author> findAuthorByName(String text) {
        authorRepository.findAuthorByName(text)
    }

    @Transactional
    Page<Author> findAuthorsByNameContainingIgnoreCase(PageRequest pageRequest, String text) {
        authorRepository.findAuthorsByNameContainingIgnoreCase(pageRequest, text)
    }
}
