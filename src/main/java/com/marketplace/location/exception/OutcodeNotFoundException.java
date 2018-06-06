package com.marketplace.location.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class OutcodeNotFoundException extends ResourceNotFoundException {
     public OutcodeNotFoundException(final String outcode) {
          super("Outcode [ " + outcode + " ] not found");
     }
}
