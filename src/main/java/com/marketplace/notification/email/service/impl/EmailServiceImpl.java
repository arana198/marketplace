package com.marketplace.notification.email.service.impl;

import com.marketplace.notification.email.domain.EmailBO;
import com.marketplace.notification.email.domain.EmailBO.Status;
import com.marketplace.notification.email.domain.EmailNotificationBO;
import com.marketplace.notification.email.dto.EmailRequest;
import com.marketplace.notification.email.exception.EmailFailedException;
import com.marketplace.notification.email.service.EmailService;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Data
@Service
class EmailServiceImpl implements EmailService {

    private final EmailNotificationRepository emailNotificationRepository;
    private final EmailSenderService emailSenderService;
    private final EmailRequestConverter emailRequestConverter;
    private final MailContentBuilder mailContentBuilder;

    @Scheduled(fixedDelay = 10000)
    public void brokerEmailVerification() {
        Map map = new HashMap<>();
        map.put("message", "http://tokenvalue.com");
        final String content = mailContentBuilder.build("brokerEmailVerification", map);
        //this.sendEmail("123", new EmailRequest("accounts@codenest.uk", "accounts@codenest.uk", "Template", content));
    }


    //email verification - broker + admin
    //reset password
    //welcome email user

    @Override
    public void sendEmail(final String sentTo, final EmailRequest emailRequest) {

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
}
