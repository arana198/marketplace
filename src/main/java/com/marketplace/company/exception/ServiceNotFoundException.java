package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class ServiceNotFoundException extends ResourceNotFoundException {
    public ServiceNotFoundException(final String serviceId) {
        super("Service [ " + serviceId + " ] does not exists");
    }
}
