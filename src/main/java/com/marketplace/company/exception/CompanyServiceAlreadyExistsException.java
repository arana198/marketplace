package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceAlreadyExistsException;

public class CompanyServiceAlreadyExistsException extends ResourceAlreadyExistsException {
  public CompanyServiceAlreadyExistsException(final String serviceId) {
    super("Company service [ " + serviceId + " ] already exists");
  }
}
