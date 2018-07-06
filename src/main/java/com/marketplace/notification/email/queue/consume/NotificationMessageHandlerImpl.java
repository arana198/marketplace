package com.marketplace.notification.email.queue.consume;

import com.google.gson.Gson;
import com.marketplace.company.dto.InviteBrokerTokenVerificationResponse;
import com.marketplace.notification.email.service.EmailService;
import com.marketplace.user.dto.TokenVerificationResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
class NotificationMessageHandlerImpl implements MessageHandler {

    private final Gson gson;
    private final EmailService emailService;

    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
        LOGGER.debug("Message action is {} and message payload is {}", message.getHeaders().get("action"), message.getPayload());
        if (message.getPayload() instanceof String) {
            Optional.ofNullable(message.getHeaders())
                    .map(header -> header.get("action"))
                    .map(Object::toString)
                    .flatMap(NotificationConsumeAction::getActionFromString)
                    .ifPresent(consumedAction -> {
                        final String payload = (String) message.getPayload();

                        switch (consumedAction) {
                            case VERIFY_EMAIL:
                                TokenVerificationResponse tokenVerificationResponse = gson.fromJson(payload, TokenVerificationResponse.class);
                                emailService.sendVerificationEmail(tokenVerificationResponse);
                                break;
                            case FORGOTTEN_PASSWORD:
                                tokenVerificationResponse = gson.fromJson(payload, TokenVerificationResponse.class);
                                emailService.sendForgottenPasswordToken(tokenVerificationResponse);
                                break;
                            case INVITE_BROKER:
                                InviteBrokerTokenVerificationResponse inviteBrokerTokenVerification = gson.fromJson(payload, InviteBrokerTokenVerificationResponse.class);
                                emailService.sendInviteEmailToBroker(inviteBrokerTokenVerification);
                                break;
                            default:
                                break;
                        }
                    });
        }
    }
}
