package com.marketplace.user.exception;

import com.marketplace.common.exception.ResourceAlreadyExistsException;

public class UserAlreadyExistsException extends ResourceAlreadyExistsException {
    public UserAlreadyExistsException(final String username) {
        super("User [ " + username + " ] already exists");
    }
}
