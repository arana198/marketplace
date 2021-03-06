package com.marketplace.user.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class UsernameNotFoundException extends ResourceNotFoundException {
     public UsernameNotFoundException(final String username) {
          super("User [ " + username + " ] not found");
     }
}
