package com.marketplace.common.exception;

public abstract class ResourceAlreadyExistsException extends Exception {
  public ResourceAlreadyExistsException(final String message) {
    super(message);
  }
}
