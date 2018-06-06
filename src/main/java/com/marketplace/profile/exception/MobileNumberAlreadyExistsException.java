package com.marketplace.profile.exception;

import com.marketplace.common.exception.ResourceAlreadyExistsException;

public class MobileNumberAlreadyExistsException extends ResourceAlreadyExistsException {
     public MobileNumberAlreadyExistsException(final String mobileNumber) {
          super("Mobile [ " + mobileNumber + " ] already in use");
     }
}
