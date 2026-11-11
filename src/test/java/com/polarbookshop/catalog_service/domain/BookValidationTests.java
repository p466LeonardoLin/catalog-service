package com.polarbookshop.catalog_service.domain;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import com.polarbookshop.catalog_service.domain.Book;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookValidationTests {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        var book = new Book("1234567890", "Title", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        var book = new Book("a234567890", "Title", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("The ISBN format must be valid.");
    }

    @Test
    void whenTitleIsBlankThenValidationFails() {
        var book = new Book("1234567890", "", "Author", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("The book title must be defined.");
    }

    @Test
    void whenAuthorIsBlankThenValidationFails() {
        var book = new Book("1234567890", "Title", "", 9.90);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("The book author must be defined.");
    }

    @Test
    void whenPriceIsNullThenValidationFails() {
        var book = new Book("1234567890", "Title", "Author", null);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("The book price must be defined.");
    }

    @Test
    void whenPriceIsZeroThenValidationFails() {
        var book = new Book("1234567890", "Title", "Author", 0.0);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("The book price must be greater than zero.");
    }

    @Test
    void whenPriceIsNegativeThenValidationFails() {
        var book = new Book("1234567890", "Title", "Author", -5.0);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
            .isEqualTo("The book price must be greater than zero.");
    }

    // e3

    private final Book.IsbnValidator isbnValidator = new Book.IsbnValidator();


    @Test
    void validIsbn10EndingInXIsAccepted() {
        assertThat(isbnValidator.isValid("080442957X", null)).isTrue();
    }

    @Test
    void invalidIsbn10IsRejected() {
        assertThat(isbnValidator.isValid("0306406153", null)).isFalse();
    }

    @Test
    void validIsbn13IsAccepted() {
        assertThat(isbnValidator.isValid("9780306406157", null)).isTrue();
    }

    @Test
    void invalidIsbn13IsRejected() {
        assertThat(isbnValidator.isValid("9780306406158", null)).isFalse();
    }

    @Test
    void lettersAreRejected() {
        assertThat(isbnValidator.isValid("abcdefghij", null)).isFalse();
    }
}