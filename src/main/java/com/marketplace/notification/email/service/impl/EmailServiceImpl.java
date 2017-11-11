package com.marketplace.notification.email.service.impl;

import com.marketplace.notification.email.dto.EmailRequest;
import com.marketplace.notification.email.exception.EmailFailedException;
import com.marketplace.notification.email.service.EmailService;
import com.marketplace.notification.email.service.impl.EmailBO.Status;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Data
@Service
class EmailServiceImpl implements EmailService {

    private final EmailNotificationRepository emailNotificationRepository;
    private final EmailSenderService emailSenderService;
    private final EmailRequestConverter emailRequestConverter;

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
