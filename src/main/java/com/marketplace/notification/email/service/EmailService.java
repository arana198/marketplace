package com.marketplace.notification.email.service;

import com.marketplace.notification.email.dto.EmailRequest;

public interface EmailService {
    void sendEmail(String sentTo, EmailRequest emailRequest);
}
