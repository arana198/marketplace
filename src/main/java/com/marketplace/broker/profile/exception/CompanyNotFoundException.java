package com.marketplace.broker.profile.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class CompanyNotFoundException extends ResourceNotFoundException {
    public CompanyNotFoundException(final String companyId) {
        super("Company [ " + companyId + " ] not found");
    }
}
