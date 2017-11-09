package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceAlreadyExistsException;

public class CompanyAlreadyExistsException extends ResourceAlreadyExistsException {
    public CompanyAlreadyExistsException(final String companyName) {
        super("Company [ " + companyName + " ] already exists");
    }

    public CompanyAlreadyExistsException(final String companyNumber, final String vatNumber) {
        super("Company number [ " + companyNumber + " ] or VAT number [ " + vatNumber + " ] already exists");
    }
}
