package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceAlreadyExistsException;

public class BrokerAlreadyRegisteredException extends ResourceAlreadyExistsException {
    public BrokerAlreadyRegisteredException(final String userId) {
        super("User [ " + userId + " ] already registered as a broker");
    }
}
