package com.marketplace.user.exception;

import com.marketplace.common.exception.BadRequestException;

import java.time.LocalDateTime;

public class UserPasswordTokenExpiredException extends BadRequestException {
     public UserPasswordTokenExpiredException(final String userId, final String token, final LocalDateTime createdTs) {
          super("User password token for user [ " + userId + " ] and token [ " + token + " ] expired at [ " + createdTs + " ]");
     }
}
