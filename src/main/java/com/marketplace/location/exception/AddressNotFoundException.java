package com.marketplace.location.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class AddressNotFoundException extends ResourceNotFoundException {
     public AddressNotFoundException(final String postcode) {
          super("Address not found for location [ " + postcode + " ]");
     }
}
