package com.marketplace.profile.exception;

import com.marketplace.common.exception.ResourceAlreadyExistsException;

public class UserProfileAlreadyExistsException extends ResourceAlreadyExistsException {
    public UserProfileAlreadyExistsException(final String userId) {
        super("UserRequest company for user [ " + userId + " ] already exists");
    }
}
