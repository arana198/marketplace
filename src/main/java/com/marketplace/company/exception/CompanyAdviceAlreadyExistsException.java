package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceAlreadyExistsException;

public class CompanyAdviceAlreadyExistsException extends ResourceAlreadyExistsException {
  public CompanyAdviceAlreadyExistsException(final String adviceId) {
    super("Company advice [ " + adviceId + " ] already exists");
  }
}
