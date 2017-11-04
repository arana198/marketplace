package com.marketplace.common.exception;

public abstract class ResourceForbiddenException extends Exception {
    public ResourceForbiddenException(final String message) {
        super(message);
    }
}
