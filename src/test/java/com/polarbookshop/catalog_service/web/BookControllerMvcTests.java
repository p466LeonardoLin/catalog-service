package com.polarbookshop.catalog_service.web;

import com.polarbookshop.catalog_service.domain.BookNotFoundException;
import com.polarbookshop.catalog_service.domain.BookService;

import com.polarbookshop.catalog_service.web.BookController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import org.springframework.http.MediaType;
import com.polarbookshop.catalog_service.domain.Book;

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

    //exercise 1:
    @Test
    void put_new_book_with201() throws Exception {
        var book1 = new Book("1357913579", "Title", "Author", 3.00);
        given(bookService.bookExists("1357913579")).willReturn(false);
        given(bookService.editBookDetails(eq("1357913579"), any(Book.class))).willReturn(book1);
        mockMvc.perform(put("/books/1357913579")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"isbn":"1357913579","title":"Title","author":"Author","price":3.00}
                    """))
            .andExpect(status().isCreated());
    }

    @Test
    void put_existing_200() throws Exception {
        var book2 = new Book("2468024680", "Title", "Author", 9.90);
        given(bookService.bookExists("2468024680")).willReturn(true);
        given(bookService.editBookDetails(eq("2468024680"), any(Book.class))).willReturn(book2);
        mockMvc.perform(put("/books/2468024680")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"isbn":"2468024680","title":"Title","author":"Author","price":9.90}
                    """))
            .andExpect(status().isOk());
    }

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