package com.bookshelf.app.controller

import com.bookshelf.app.model.Author
import com.bookshelf.app.model.Book
import com.bookshelf.app.model.Publisher
import com.bookshelf.app.service.AuthorService
import com.bookshelf.app.service.BookService
import com.bookshelf.app.service.PublisherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import org.jsoup.*

@RestController
@RequestMapping("/api")
class DiscoveryController {

    @Autowired
    AuthorService authorService

    @Autowired
    BookService bookService

    @Autowired
    PublisherService publisherService

    @GetMapping("/discover")
    ResponseEntity<Map<String, Object>> discover(
            @RequestParam(defaultValue = "") String searchQuery
    ) {
        Map<String, Object> response = [searchQuery: searchQuery]

        try {
            def doc = Jsoup.connect(searchQuery).get()

            def tables = doc.select("table")
            def table = tables[2]
            def rows = table.select("tr")

            def rowList = []

            rows.eachWithIndex {row, idx ->
                Map<String, Object> rowMap = [:]

                def tds = row.select("td")

                def authors_data = tds[1]
                def book_data = tds[2]
                def publisher_data = tds[3]
                def year_data = tds[4]
                def language_data = tds[6]

                ArrayList<String> authors = []

                authors_data.select("a").each { a->
                    if(!a.text().isEmpty()) {
                        authors.add(a.text())
                    }
                }

                Map<String, String> books = [:]

                book_data.select("a").each { b->
                    if(!b.wholeText().isEmpty()) {
                        books['title'] = b.wholeText()
                    }
                }

                books['publisher'] = publisher_data.text()
                books['language'] = language_data.text()
                books['year'] = year_data.text()

                rowMap['book'] = books
                rowMap['authors'] = authors

                rowList.add(rowMap)
            }

            def new_books = 0
            def existing_books = 0

            def newBooks = []
            def existingBooks = []

            for(int i = 1; i < rowList.size(); i++) {
                def book = rowList[i]['book'] as Map<String, String>
                def authors = rowList[i]['authors'] as ArrayList<String>

                def book_ = bookService.findBookByTitle(book['title'])
                def publisher_ = publisherService.findPublisherByName(book['publisher'])

                if(book_.isPresent()) {
                    existing_books += 1
                    existingBooks.add(book_.get())
                } else {
                    new_books += 1

                    def newBook = new Book()

                    newBook.title = book['title']
                    newBook.language = book['language']
                    newBook.year = book['year']

                    Publisher bookPublisher = null
                    Set<Author> bookAuthors = []

                    if(!publisher_.isPresent()) {
                        bookPublisher = new Publisher()
                        bookPublisher.name = book['publisher']
                        bookPublisher = publisherService.create(bookPublisher)

                        if(book['publisher'].trim().size() != 0)
                            newBook.publisher = bookPublisher
                        else
                            newBook.publisher = null
                    } else {
                        newBook.publisher = publisher_.get()
                    }

                    authors.each { author_name ->
                        def author = new Author()
                        author.name = author_name

                        def author_ = authorService.findAuthorByName(author_name)
                        if(!author_.isPresent()) {
                            bookAuthors.add(authorService.create(author))
                        } else {
                            bookAuthors.add(author_.get())
                        }
                    }

                    newBook.authors = bookAuthors

                    newBooks.add(bookService.create(newBook))
                }
            }

            response['new-books'] = newBooks
            response['existing-books'] = existingBooks
        } catch (Exception e) {
            e.printStackTrace(System.err)
            return new ResponseEntity<Map<String, Object>>([error: e.toString()], HttpStatus.INTERNAL_SERVER_ERROR)
        }

        return new ResponseEntity<>(response, HttpStatus.OK)
    }
}
