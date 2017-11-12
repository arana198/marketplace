package com.marketplace.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class ConfigurationService {
    @Value("${oauth.authzEndpoint}")
    private String swaggerOAuthUrl;

    @Value("${oauth.clientId}")
    private String oAuthClientId;

    @Value("${oauth.clientSecret}")
    private String oAuthClientSecret;

    @Value("${validator.api.company.url}")
    private String companyValidatorUrlPrefix;

    @Value("${validator.api.company.key}")
    private String companyValidatorApiKey;

    @Value("${validator.api.vat.url}")
    private String vatValidatorUrlPrefix;

    @Value("${mail.api.url}")
    private String emailApiUrl;

    @Value("${mail.api.token}")
    private String emailApiToken;
}
