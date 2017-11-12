package com.marketplace.notification.email.service.impl;


import com.marketplace.notification.email.dto.EmailRequest;
import com.marketplace.notification.email.exception.EmailFailedException;
import com.marketplace.utils.ConfigurationService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Data
@Service
class EmailSenderService {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private final ConfigurationService configurationService;

    public void send(final EmailRequest emailRequest) throws EmailFailedException {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("Authorization", configurationService.getEmailApiToken());
        headers.add("Content-Type", "application/json");

        HttpEntity<EmailRequest> request = new HttpEntity<EmailRequest>(emailRequest, headers);
        try {
            REST_TEMPLATE.postForEntity(configurationService.getEmailApiUrl(), request, String.class);
        } catch (HttpServerErrorException ex) {
            log.warn("Email failed to sent: ", ex);
            throw ex;
        } catch (HttpClientErrorException ex) {
            throw new EmailFailedException(ex.getMessage());
        }
    }
}
