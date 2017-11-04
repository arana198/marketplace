package com.marketplace.broker.profile.validator;

import com.marketplace.utils.ConfigurationService;
import org.springframework.stereotype.Service;

@Service
class CompanyValidatorImpl implements CompanyValidator {

    private final ConfigurationService configurationService;

    public CompanyValidatorImpl(final ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    public boolean validate(final String companyName, final String companyNumber) {
return true;
    }
}

