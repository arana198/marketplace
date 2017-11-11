package com.marketplace.notification.email.exception;

public class EmailFailedException extends Exception {
    public EmailFailedException(final String message) {
        super("Email failed to sent: " + message);
    }
}
