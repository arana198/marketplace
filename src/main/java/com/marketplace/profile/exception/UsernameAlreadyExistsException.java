package com.marketplace.profile.exception;

import com.marketplace.common.exception.ResourceAlreadyExistsException;

public class UsernameAlreadyExistsException extends ResourceAlreadyExistsException {
    public UsernameAlreadyExistsException(final String email) {
        super("Email [ " + email + " ] already in use");
    }
}
