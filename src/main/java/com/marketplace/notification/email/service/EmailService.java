package com.marketplace.notification.email.service;

import com.marketplace.user.dto.TokenVerificationResponse;

public interface EmailService {
    void sendVerificationEmail(TokenVerificationResponse tokenVerificationResponse);

    void sendForgottenPasswordToken(TokenVerificationResponse tokenVerificationResponse);
}
