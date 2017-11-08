package com.marketplace.user.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(final String userId) {
        super("UserRequest [ " + userId + " ] not found");
    }
}
