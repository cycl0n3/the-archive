package com.bookshelf.app.controller

import com.bookshelf.app.model.Author
import com.bookshelf.app.model.Book
import com.bookshelf.app.service.AuthorService
import com.bookshelf.app.service.BookService
import com.bookshelf.app.service.PublisherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class BookController {

	@Autowired
	AuthorService authorService

	@Autowired
	BookService bookService

	@GetMapping("/books")
	ResponseEntity<Map<String, Object>> findAllBooks(
			@RequestParam(defaultValue = "") String searchQuery,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "25") int size
	) {
		try {
			Page<Book> booksPage;
			def paging = PageRequest.of(page, size)

			if(searchQuery.isBlank()) {
				booksPage = bookService.findAll(paging)
			} else {
				booksPage = bookService.findBooksByTitleContainingIgnoreCase(paging, searchQuery)
			}

			Map<String, Object> response = [:]

			response['books'] = booksPage.content
			response['currentPage'] = booksPage.number
			response['totalItems'] = booksPage.totalElements
			response['totalPages'] = booksPage.totalPages

			return new ResponseEntity<>(response, HttpStatus.OK)
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(['error': e.toString()], HttpStatus.INTERNAL_SERVER_ERROR)
		}
	}
	
	@GetMapping("/books/{id}")
	ResponseEntity<Map<String, Object>> findBookById(@PathVariable long id) {
		try {
			def book = bookService.findById(id)

			Map<String, Object> response = [:]

			if (book.isPresent()) {
				response['book'] = book.get()
				return new ResponseEntity<>(response, HttpStatus.OK)
			}

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND)
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(['error': e.toString()], HttpStatus.INTERNAL_SERVER_ERROR)
		}
	}

	@GetMapping("/books/{id}/authors")
	ResponseEntity<Map<String, Object>> findAuthorsByBookId(@PathVariable long id) {
		try {
			def book = bookService.findById(id)

			Map<String, Object> response = [:]

			if (book.isPresent()) {
				response['authors'] = book.get().authors.toList()
				return new ResponseEntity<>(response, HttpStatus.OK)
			}

			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND)
		} catch (Exception e) {
			return new ResponseEntity<Map<String, Object>>(['error': e.toString()], HttpStatus.INTERNAL_SERVER_ERROR)
		}
	}
}
