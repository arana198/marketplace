package com.marketplace.user.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class UserPasswordTokenNotFoundException extends ResourceNotFoundException {
    public UserPasswordTokenNotFoundException(final String userId, final String token) {
        super("Password token for user [ " + userId + " ] and token [ " + token + " ] not found");
    }
}
