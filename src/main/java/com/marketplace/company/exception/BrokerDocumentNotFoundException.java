package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class BrokerDocumentNotFoundException extends ResourceNotFoundException {
    public BrokerDocumentNotFoundException(final String brokerDocumentId) {
        super("Broker document [ " + brokerDocumentId + " ] not found");
    }
}
