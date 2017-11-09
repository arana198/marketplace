package com.marketplace.company.validator;

import com.marketplace.company.dto.VATVerificationResponse;
import com.marketplace.utils.ConfigurationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
class VATValidatorImpl implements VATValidator {

    private final String API_URL;
    private final RestTemplate restTemplate;

    public VATValidatorImpl(ConfigurationService configurationService) {
        this.restTemplate = new RestTemplate();
        this.API_URL = configurationService.getVatValidatorUrlPrefix() + "/GB/";
    }

    @Override
    public boolean validate(final String companyName, final String vatNumber) {
        final String url = API_URL + vatNumber;

        try {
            final VATVerificationResponse vatVerificationResponse = this.restTemplate.getForObject(url, VATVerificationResponse.class);
            return vatVerificationResponse.isValid() && companyName.trim().equalsIgnoreCase(vatVerificationResponse.getName().getName().trim());
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw ex;
            }
        }

        return false;
    }
}
