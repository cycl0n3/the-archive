package com.bookshelf.app.controller

import com.bookshelf.app.model.Author
import com.bookshelf.app.service.AuthorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AuthorController {

    @Autowired
    AuthorService authorService

    @GetMapping("/authors")
    ResponseEntity<Map<String, Object>> findAllAuthors(
            @RequestParam(defaultValue = "") String searchQuery,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size
    ) {
        try {
            Page<Author> authorPage
            def paging = PageRequest.of(page, size)

            if (searchQuery.isBlank()) {
                authorPage = authorService.findAll(paging)
            } else {
                authorPage = authorService.findAuthorsByNameContainingIgnoreCase(paging, searchQuery)
            }

            Map<String, Object> response = [:]
            response['authors'] = authorPage.content

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK)
        } catch (Exception e) {
            return new ResponseEntity<Map<String, Object>>(['error': e.toString()], HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @GetMapping("/authors/{id}")
    ResponseEntity<Map<String, Object>> findById(@PathVariable long id) {
        try {
            def author = authorService.findById(id)

            Map<String, Object> response = [:]

            if (author.isPresent()) {
                response['author'] = author.get()
                response['books'] = author.get().books

                return new ResponseEntity<>(response, HttpStatus.OK)
            }

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND)
        } catch (Exception e) {
            return new ResponseEntity<Map<String, Object>>(['error': e.toString()], HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
