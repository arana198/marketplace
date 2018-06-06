package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceAlreadyExistsException;

public class ServiceAlreadyExistsException extends ResourceAlreadyExistsException {
     public ServiceAlreadyExistsException(final String name) {
          super("Service [ " + name + " ] already exists");
     }
}
