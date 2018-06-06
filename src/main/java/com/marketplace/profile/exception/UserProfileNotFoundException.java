package com.marketplace.profile.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class UserProfileNotFoundException extends ResourceNotFoundException {
     public UserProfileNotFoundException(final String userId, final String profileId) {
          super("User company for user [ " + userId + " ] and [ " + profileId + " ] not found");
     }
}
