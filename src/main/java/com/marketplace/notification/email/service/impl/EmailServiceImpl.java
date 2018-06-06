package com.marketplace.notification.email.service.impl;

import com.marketplace.company.dto.InviteBrokerTokenVerificationResponse;
import com.marketplace.notification.email.domain.EmailBO;
import com.marketplace.notification.email.domain.EmailBO.Status;
import com.marketplace.notification.email.domain.EmailNotificationBO;
import com.marketplace.notification.email.dto.EmailRequest;
import com.marketplace.notification.email.exception.EmailFailedException;
import com.marketplace.notification.email.service.EmailService;
import com.marketplace.user.dto.TokenVerificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Service
class EmailServiceImpl implements EmailService {

     private static final String FROM_EMAIL = "accounts@codenest.uk";

     private final EmailNotificationRepository emailNotificationRepository;
     private final EmailSenderService emailSenderService;
     private final EmailRequestConverter emailRequestConverter;
     private final MailContentBuilder mailContentBuilder;

     //TODO: welcome email user

     @Override
     public void sendInviteEmailToBroker(final InviteBrokerTokenVerificationResponse inviteBrokerTokenVerificationResponse) {
          final Map map = new HashMap<>();
          map.put("companyId", inviteBrokerTokenVerificationResponse.getCompanyId());
          map.put("companyName", inviteBrokerTokenVerificationResponse.getCompanyName());
          map.put("token", inviteBrokerTokenVerificationResponse.getToken());
          final String content = mailContentBuilder.build("inviteBrokerEmail", map);
          final EmailRequest emailRequest = this.getEmailRequest(FROM_EMAIL, inviteBrokerTokenVerificationResponse.getEmail(),
              "Invitation from " + inviteBrokerTokenVerificationResponse.getCompanyName(), content);

          this.sendEmail(inviteBrokerTokenVerificationResponse.getEmail(), emailRequest);
     }

     @Override
     public void sendVerificationEmail(final TokenVerificationResponse tokenVerificationResponse) {
          final Map map = new HashMap<>();
          map.put("token", tokenVerificationResponse.getToken());
          final String content = mailContentBuilder.build("brokerEmailVerification", map);
          this.sendEmail(tokenVerificationResponse.getEmail(), this.getEmailRequest(FROM_EMAIL, tokenVerificationResponse.getEmail(), "Email Verification", content));
     }

     @Override
     public void sendForgottenPasswordToken(final TokenVerificationResponse tokenVerificationResponse) {
          final Map map = new HashMap<>();
          map.put("token", tokenVerificationResponse.getToken());
          final String content = mailContentBuilder.build("forgottenPassword", map);
          this.sendEmail(tokenVerificationResponse.getEmail(), this.getEmailRequest(FROM_EMAIL, tokenVerificationResponse.getEmail(), "Forgotten Password", content));
     }

     private void sendEmail(final String sentTo, final EmailRequest emailRequest) {

          final EmailNotificationBO emailNotificationBO = new EmailNotificationBO();
          final EmailBO emailBO = emailRequestConverter.convert(emailRequest);
          emailNotificationBO.setEmailBO(emailBO);
          emailNotificationBO.setSentTo(sentTo);

          try {
               emailSenderService.send(emailRequest);
               emailBO.setStatus(Status.SUCCESS);
          } catch (EmailFailedException e) {
               emailBO.setStatus(Status.FAILED);
               emailBO.setErrorMessage(e.getMessage());
          }

          emailNotificationRepository.save(emailNotificationBO);
     }

     private EmailRequest getEmailRequest(final String from, final String to, final String subject, final String content) {
          return new EmailRequest(from, to, subject, content);
     }
}
