package com.polarbookshop.catalog_service.web;

import com.polarbookshop.catalog_service.domain.Book;
import com.polarbookshop.catalog_service.domain.BookAlreadyExistsException;
import com.polarbookshop.catalog_service.domain.BookNotFoundException;
import com.polarbookshop.catalog_service.domain.BookService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "7373731394";
        given(bookService.viewBookDetails(isbn))
            .willThrow(BookNotFoundException.class);

        mockMvc.perform(get("/books/" + isbn))
            .andExpect(status().isNotFound());
    }

    @Test
    void whenPostDuplicateBookThenShouldReturn422() throws Exception {
        given(bookService.addBookToCatalog(any(Book.class)))
            .willThrow(new BookAlreadyExistsException("1234567890"));

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"isbn":"1234567890","title":"Title","author":"Author","price":9.90}
                    """))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenPostInvalidBookThenShouldReturn400() throws Exception {
        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"isbn":"1234567890","title":"","author":"Author","price":9.90}
                    """))
            .andExpect(status().isBadRequest());
    }

    // Exercise 1: PUT answers 201 when it creates, 200 when it updates.
    @Test
    void put_new_book_with201() throws Exception {
        var book1 = Book.of("1357913579", "Title", "Author", 3.00);
        given(bookService.bookExists("1357913579")).willReturn(false);
        given(bookService.editBookDetails(eq("1357913579"), any(Book.class)))
            .willReturn(book1);

        mockMvc.perform(put("/books/1357913579")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"isbn":"1357913579","title":"Title","author":"Author","price":3.00}
                    """))
            .andExpect(status().isCreated());
    }

    @Test
    void put_existing_200() throws Exception {
        var book2 = Book.of("2468024680", "Title", "Author", 9.90);
        given(bookService.bookExists("2468024680")).willReturn(true);
        given(bookService.editBookDetails(eq("2468024680"), any(Book.class)))
            .willReturn(book2);

        mockMvc.perform(put("/books/2468024680")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"isbn":"2468024680","title":"Title","author":"Author","price":9.90}
                    """))
            .andExpect(status().isOk());
    }

    // Exercise 2: path ISBN and body ISBN must match.
    @Test
    void whenPutWithMismatchedIsbnThenShouldReturn400AndNotTouchService() throws Exception {
        mockMvc.perform(put("/books/1234512345")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"isbn":"1111111111","title":"Title","author":"Author","price":9.90}
                    """))
            .andExpect(status().isBadRequest());

        verifyNoInteractions(bookService);
    }
}