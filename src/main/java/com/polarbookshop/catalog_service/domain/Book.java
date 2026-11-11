package com.polarbookshop.catalog_service.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

public record Book(

    @NotBlank(message = "The book ISBN must be defined.")
    @Pattern(regexp = "^([0-9]{10}|[0-9]{13})$",
             message = "The ISBN format must be valid.")
    String isbn,

    @NotBlank(message = "The book title must be defined.")
    String title,

    @NotBlank(message = "The book author must be defined.")
    String author,

    @NotNull(message = "The book price must be defined.")
    @Positive(message = "The book price must be greater than zero.")
    Double price
) {//e3: in case to be divisible by 11 and 10 for 10 13 isbn.
    @Constraint(validatedBy = IsbnValidator.class)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ValidIsbn {
        String message() default "check";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    public static class IsbnValidator implements ConstraintValidator<ValidIsbn, String> {

        @Override
        public boolean isValid(String isbn, ConstraintValidatorContext context) {
            if (isbn == null) {
                return true;
            }
            if (isbn.length() == 10) {
                return isValidIsbn10(isbn);
            }
            if (isbn.length() == 13) {
                return isValidIsbn13(isbn);
            }
            return false;
        }

        private boolean isValidIsbn10(String isbn) {
            int sum = 0;
            for (int i = 0; i < 10; i++) {
                char c = isbn.charAt(i);
                int value;
                if (c >= '0' && c <= '9') {
                    value = c - '0';
                } else if (c == 'X' && i == 9) {
                    value = 10;
                } else {
                    return false;
                }
                sum += value * (10 - i);
            }
            return sum % 11 == 0;
        }

        private boolean isValidIsbn13(String isbn) {
            int sum = 0;
            for (int i = 0; i < 13; i++) {
                char c = isbn.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
                int digit = c - '0';
                if (i % 2 == 0) {
                    sum += digit;
                } else {
                    sum += digit * 3;
                }
            }
            return sum % 10 == 0;
        }
    }
    //i'm not sure if i just need to chek for modulus 11 and 10 or just count them as they are 10 and 13 digits.
}