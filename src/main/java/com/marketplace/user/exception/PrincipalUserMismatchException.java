package com.marketplace.user.exception;

import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;

public class PrincipalUserMismatchException extends UnauthorizedUserException {

    private static final long serialVersionUID = -5464030706060936824L;

    public PrincipalUserMismatchException(final String message) {
        super(message);
    }
}
