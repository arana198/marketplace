package com.marketplace.location.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class PostcodeNotFoundException extends ResourceNotFoundException {
  public PostcodeNotFoundException(final String postcode) {
    super("Postcode [ " + postcode + " ] not found");
  }
}
