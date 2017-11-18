package com.marketplace.user.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class EmailVerificationTokenNotFoundException extends ResourceNotFoundException {
    public EmailVerificationTokenNotFoundException(final String token) {
        super("Email verification token [ " + token + " ] not found");
    }
}
