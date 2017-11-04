package com.marketplace.broker.profile.validator;

import com.marketplace.broker.profile.dto.VATVerificationResponse;
import com.marketplace.utils.ConfigurationService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
class VATValidatorImpl implements VATValidator {

    private final String API_URL;
    private final ConfigurationService configurationService;
    private RestTemplate restTemplate = new RestTemplate();

    public VATValidatorImpl(ConfigurationService configurationService) {
        this.configurationService = configurationService;
        this.API_URL = configurationService.getVatValidatorUrlPrefix() + "/GB/";
    }

    @Override
    public boolean validate(final String companyName, final String vatNumber) {
        final String url = API_URL + vatNumber;
        final VATVerificationResponse vatVerificationResponse = this.restTemplate.getForObject(url, VATVerificationResponse.class);
        return vatVerificationResponse.isValid() && companyName.trim().equalsIgnoreCase(vatVerificationResponse.getName().getName().trim());
    }
}
