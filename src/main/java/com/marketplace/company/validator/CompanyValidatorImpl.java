package com.marketplace.company.validator;

import com.marketplace.company.dto.CompanyVerificationResponse;
import com.marketplace.utils.ConfigurationService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
class CompanyValidatorImpl implements CompanyValidator {

    private final String API_URL;
    private final String API_KEY;
    private final RestTemplate restTemplate;

    public CompanyValidatorImpl(final ConfigurationService configurationService) {
        this.API_URL = configurationService.getCompanyValidatorUrlPrefix();
        this.API_KEY = configurationService.getCompanyValidatorApiKey();
        this.restTemplate = new RestTemplate();
    }

    public boolean validate(final String companyName, final String companyNumber) {
        final String url = API_URL + "/company/" + companyNumber;

        String base64Creds = Base64.encodeBase64String(API_KEY.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        try {
            final CompanyVerificationResponse companyVerificationResponse = this.restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), CompanyVerificationResponse.class, new HashMap<>()).getBody();
            return companyName.replaceAll(" ", "").equalsIgnoreCase(companyVerificationResponse.getName().replaceAll(" ", ""));
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw ex;
            }
        }

        return false;
    }
}

