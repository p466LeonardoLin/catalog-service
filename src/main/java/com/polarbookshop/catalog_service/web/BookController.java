package com.polarbookshop.catalog_service.web;

import com.polarbookshop.catalog_service.domain.Book;
import com.polarbookshop.catalog_service.domain.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Iterable<Book> get() {
        return bookService.viewBookList();
    }

    @GetMapping("{isbn}")
    public Book getByIsbn(@PathVariable String isbn) {
        return bookService.viewBookDetails(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book post(@Valid @RequestBody Book book) {
        return bookService.addBookToCatalog(book);
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }

    // @PutMapping("{isbn}")
    // public Book put(@PathVariable String isbn, @Valid @RequestBody Book book) {
    //     return bookService.editBookDetails(isbn, book);
    // }
    @PutMapping("{isbn}")
    public ResponseEntity<Book> put(@PathVariable String isbn, @Valid @RequestBody Book book) {
        if (!isbn.equals(book.isbn())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "wrong!, isbn not equal");
        }
        boolean ex = bookService.bookExists(isbn);
        Book sv = bookService.editBookDetails(isbn, book);
        return ex
            ? ResponseEntity.ok(sv)
            : ResponseEntity.status(HttpStatus.CREATED).body(sv);
    }
}