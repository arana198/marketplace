package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class BrokerNotFoundException extends ResourceNotFoundException {
    public BrokerNotFoundException(final String companyId, final String employeeId) {
        super("Employee [ " + employeeId + " ] for company [ " + companyId + " ] not found or expired");
    }
}
