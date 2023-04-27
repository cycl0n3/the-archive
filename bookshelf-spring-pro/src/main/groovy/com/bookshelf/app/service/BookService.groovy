package com.bookshelf.app.service

import com.bookshelf.app.model.Book
import com.bookshelf.app.repo.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service("bookService")
class BookService extends SimpleService<Book, Long> {
	
	@Autowired
	BookRepository bookRepository

	@Override
	JpaRepository<Book, Long> getRepository() {
		bookRepository
	}

	Optional<Book> findBookByTitle(String text) {
		bookRepository.findBookByTitle(text)
	}

	List<Book> findBooksByTitleContainingIgnoreCase(String text) {
		bookRepository.findBooksByTitleContainingIgnoreCase(text)
	}

	Page<Book> findBooksByTitleContainingIgnoreCase(PageRequest paging, String text) {
		bookRepository.findBooksByTitleContainingIgnoreCase(paging, text)
	}
}
