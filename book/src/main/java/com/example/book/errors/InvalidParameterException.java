package com.example.book.errors;

import org.springframework.validation.Errors;

public class InvalidParameterException extends RuntimeException {
    private final Errors errors;

    public InvalidParameterException(Errors errors) {
        super("잘못된 입력입니다.");
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
