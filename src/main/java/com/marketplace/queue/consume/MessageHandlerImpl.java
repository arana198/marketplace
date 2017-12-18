package com.marketplace.queue.consume;

import com.google.gson.Gson;
import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.company.dto.InviteBrokerTokenVerificationResponse;
import com.marketplace.notification.email.service.EmailService;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.TokenVerificationResponse;
import com.marketplace.user.dto.UserStatusRequest.UserStatus;
import com.marketplace.user.exception.UserNotFoundException;
import com.marketplace.user.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
class MessageHandlerImpl implements MessageHandler {

    private final Gson gson = new Gson();
    private final EmailService emailService;
    private final UserService userService;

    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
        log.debug("Message action is {} and message payload is {}", message.getHeaders().get("action"), message.getPayload());
        if (message.getPayload() instanceof String) {
            final ConsumeAction consumedAction = ConsumeAction.getActionFromString(message.getHeaders().get("action").toString());
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
                    InviteBrokerTokenVerificationResponse inviteBrokerTokenVerificationResponse = gson.fromJson(payload, InviteBrokerTokenVerificationResponse.class);
                    emailService.sendInviteEmailToBroker(inviteBrokerTokenVerificationResponse);
                    break;
                case BROKER_REMOVED_FROM_COMPANY:
                    BrokerProfileResponse brokerProfileResponse = gson.fromJson(payload, BrokerProfileResponse.class);
                    final UserRole userRole = brokerProfileResponse.isAdmin() ? UserRole.ROLE_COMPANY_ADMIN : UserRole.ROLE_BROKER;
                    try {
                        userService.updateUserStatus(brokerProfileResponse.getUserId(), userRole, UserStatus.CLOSED);
                    } catch (UserNotFoundException e) {
                        log.error("User {} not found", brokerProfileResponse.getUserId());
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
