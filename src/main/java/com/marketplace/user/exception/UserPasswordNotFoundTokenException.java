package com.marketplace.user.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class UserPasswordNotFoundTokenException extends ResourceNotFoundException {
    public UserPasswordNotFoundTokenException(final String userId, final String token) {
        super("UserRequest password token for user [ " + userId + " ] and token [ " + token + " ] not found");
    }
}
