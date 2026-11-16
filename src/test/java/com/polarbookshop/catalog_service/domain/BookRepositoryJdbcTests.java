package com.polarbookshop.catalog_service.domain;

import java.util.Optional;
import java.util.stream.StreamSupport;

import com.polarbookshop.catalog_service.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)            // auditing is not part of the slice by default
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
                                     // do not swap in an embedded database
@ActiveProfiles("integration")       // use the Testcontainers URL
class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;  // lower-level setup tool

    @Test
    void findBookByIsbnWhenExisting() {
        var isbn = "1234561237";
        var book = Book.of(isbn, "Title", "Author", 12.90);
        jdbcAggregateTemplate.insert(book);   // arrange without the repository under test

        Optional<Book> actual = bookRepository.findByIsbn(isbn);

        assertThat(actual).isPresent();
        assertThat(actual.get().isbn()).isEqualTo(isbn);
    }

    @Test
    void findBookByIsbnWhenNotExisting() {
        assertThat(bookRepository.findByIsbn("1234561238")).isEmpty();
    }

    @Test
    void findAllBooks() {
        var book1 = Book.of("1234561235", "Title", "Author", 12.90);
        var book2 = Book.of("1234561236", "Title", "Author", 12.90);
        jdbcAggregateTemplate.insert(book1);
        jdbcAggregateTemplate.insert(book2);

        Iterable<Book> actual = bookRepository.findAll();

        assertThat(StreamSupport.stream(actual.spliterator(), false)
            .filter(b -> b.isbn().equals(book1.isbn()) || b.isbn().equals(book2.isbn()))
            .toList()).hasSize(2);
    }

    @Test
    void existsByIsbnWhenExisting() {
        var isbn = "1234561239";
        jdbcAggregateTemplate.insert(Book.of(isbn, "Title", "Author", 12.90));

        assertThat(bookRepository.existsByIsbn(isbn)).isTrue();
    }

    @Test
    void existsByIsbnWhenNotExisting() {
        assertThat(bookRepository.existsByIsbn("1234561240")).isFalse();
    }

    @Test
    void deleteByIsbn() {
        var isbn = "1234561241";
        var persisted = jdbcAggregateTemplate.insert(Book.of(isbn, "Title", "Author", 12.90));

        bookRepository.deleteByIsbn(isbn);

        assertThat(jdbcAggregateTemplate.findById(persisted.id(), Book.class)).isNull();
    }

    @Test
    void createdDateIsSetOnSave() {
        var saved = bookRepository.save(Book.of("1234561242", "Title", "Author", 12.90));

        assertThat(saved.id()).isNotNull();
        assertThat(saved.createdDate()).isNotNull();
        assertThat(saved.lastModifiedDate()).isNotNull();
    }

    @Test
    void publisherIsPersisted() {
        bookRepository.save(Book.of("1234562004", "Title", "Author", 12.90, "Polarsophia"));

        var found = bookRepository.findByIsbn("1234562004");

        assertThat(found).isPresent();
        assertThat(found.get().publisher()).isEqualTo("Polarsophia");
    }

    @Test
    void publisherIsOptional() {
        bookRepository.save(Book.of("1234562005", "Title", "Author", 12.90));

        var found = bookRepository.findByIsbn("1234562005");

        assertThat(found).isPresent();
        assertThat(found.get().publisher()).isNull();
    }
}