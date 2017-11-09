package com.marketplace.broker.profile.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class CompanyEmployeeInviteTokenNotFoundException extends ResourceNotFoundException {
    public CompanyEmployeeInviteTokenNotFoundException(final String token) {
        super("Company employee token [ " + token + " ] not found or expired");
    }
}
