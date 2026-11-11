package com.polarbookshop.catalog_service;

import com.polarbookshop.catalog_service.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void whenPostRequestThenBookCreated() {
        var expected = new Book("1231231231", "Title", "Author", 9.90);

        webTestClient
            .post().uri("/books")
            .bodyValue(expected)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Book.class).value(actual -> {
                assertThat(actual).isNotNull();
                assertThat(actual.isbn()).isEqualTo(expected.isbn());
            });
    }
	@Test
    void whenBookLifecycleThenAllStepsWork() {
        var isbn = "9999999999";
        var book = new Book(isbn, "Title", "Author", 9.90);

        // 1 POST
        webTestClient.post().uri("/books").bodyValue(book)
            .exchange().expectStatus().isCreated();

        // 2 GET
        webTestClient.get().uri("/books/" + isbn)
            .exchange().expectStatus().isOk()
            .expectBody(Book.class)
            .value(b -> assertThat(b.title()).isEqualTo("Title"));

        // 3 PUT
        var updated = new Book(isbn, "New Title", "Author", 19.90);
        webTestClient.put().uri("/books/" + isbn).bodyValue(updated)
            .exchange().expectStatus().isOk()
            .expectBody(Book.class)
            .value(b -> assertThat(b.title()).isEqualTo("New Title"));

        // 4 DELETE
        webTestClient.delete().uri("/books/" + isbn)
            .exchange().expectStatus().isNoContent();

        // 5 GET
        webTestClient.get().uri("/books/" + isbn)
            .exchange().expectStatus().isNotFound();
    }
}