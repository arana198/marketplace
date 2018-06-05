package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class AdviceMethodNotFoundException extends ResourceNotFoundException {
  public AdviceMethodNotFoundException(final String adviceMethodId) {
    super("Advice method [ " + adviceMethodId + " ] does not exists");
  }
}
