package com.marketplace.notification.email.service;

import com.marketplace.company.dto.InviteBrokerTokenVerificationResponse;
import com.marketplace.user.dto.TokenVerificationResponse;

public interface EmailService {
     void sendInviteEmailToBroker(InviteBrokerTokenVerificationResponse inviteBrokerTokenVerificationResponse);

     void sendVerificationEmail(TokenVerificationResponse tokenVerificationResponse);

     void sendForgottenPasswordToken(TokenVerificationResponse tokenVerificationResponse);
}
