package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class CompanyEmployeeNotFoundException extends ResourceNotFoundException {
    public CompanyEmployeeNotFoundException(final String companyId, final String employeeId) {
        super("Employee [ " + employeeId + " ] for company [ " + companyId + " ] not found or expired");
    }
}
