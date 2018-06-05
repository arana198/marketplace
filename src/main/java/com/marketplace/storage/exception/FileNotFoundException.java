package com.marketplace.storage.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class FileNotFoundException extends ResourceNotFoundException {
  public FileNotFoundException(final String id) {
    super("File [ " + id + " ] not found");
  }
}
