package com.marketplace.common.exception;

public class ResourceForbiddenException extends RuntimeException {
    public ResourceForbiddenException(final String message) {
        super(message);
    }
}
