package com.marketplace.common.exception;

import org.springframework.validation.Errors;

public class BadRequestException extends RuntimeException {

    private Errors errors;

    public BadRequestException(final String message) {
        super(message);
    }

    public BadRequestException(final String message, final Errors errors) {
        super(message);
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
