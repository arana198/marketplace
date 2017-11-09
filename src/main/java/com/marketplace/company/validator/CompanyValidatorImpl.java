package com.marketplace.company.validator;

import com.marketplace.company.dto.CompanyVerificationResponse;
import com.marketplace.utils.ConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
class CompanyValidatorImpl implements CompanyValidator {

    private final String API_URL;
    private final String API_KEY;
    private final RestTemplate restTemplate;

    public CompanyValidatorImpl(final ConfigurationService configurationService) {
        this.API_URL = configurationService.getCompanyValidatorUrlPrefix() + "/GB/";
        this.API_KEY = configurationService.getCompanyValidatorApiKey();
        this.restTemplate = new RestTemplate();
    }

    public boolean validate(final String companyName, final String companyNumber) {
        final String url = API_URL + "/company/" + companyNumber;

        try {
            final CompanyVerificationResponse companyVerificationResponse = this.restTemplate.getForObject(url, CompanyVerificationResponse.class);
            return companyName.trim().equalsIgnoreCase(companyVerificationResponse.getName().trim());
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw ex;
            }
        }

        return false;
    }
}

