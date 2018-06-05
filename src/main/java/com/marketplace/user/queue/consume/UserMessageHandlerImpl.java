package com.marketplace.user.queue.consume;

import com.google.gson.Gson;
import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.UserStatusRequest.UserStatus;
import com.marketplace.user.exception.UserNotFoundException;
import com.marketplace.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
class UserMessageHandlerImpl implements MessageHandler {

  private final Gson gson;
  private final UserService userService;

  @Override
  public void handleMessage(final Message<?> message) throws MessagingException {
    LOGGER.debug("Message action is {} and message payload is {}", message.getHeaders().get("action"), message.getPayload());
    if (message.getPayload() instanceof String) {
      UserConsumeAction.getActionFromString(message.getHeaders().get("action").toString())
          .ifPresent(consumedAction -> {
            final String payload = (String) message.getPayload();

            switch (consumedAction) {
              case BROKER_ADDED_AS_ADMIN:
                BrokerProfileResponse brokerProfileResponse = gson.fromJson(payload, BrokerProfileResponse.class);
                userService.addAsCompanyAdmin(brokerProfileResponse.getUserId());
                break;
              case BROKER_REMOVED_AS_ADMIN:
                brokerProfileResponse = gson.fromJson(payload, BrokerProfileResponse.class);
                userService.removeAsCompanyAdmin(brokerProfileResponse.getUserId());
                break;
              case BROKER_REMOVED_FROM_COMPANY:
                brokerProfileResponse = gson.fromJson(payload, BrokerProfileResponse.class);
                UserRole userRole = brokerProfileResponse.isAdmin() ? UserRole.ROLE_COMPANY_ADMIN : UserRole.ROLE_BROKER;

                try {
                  userService.updateUserStatus(brokerProfileResponse.getUserId(), userRole, UserStatus.CLOSED);
                } catch (UserNotFoundException e) {
                  LOGGER.error("User {} not found", brokerProfileResponse.getUserId());
                }

                break;
              case BROKER_VERIFIED:
                brokerProfileResponse = gson.fromJson(payload, BrokerProfileResponse.class);
                userRole = brokerProfileResponse.isAdmin() ? UserRole.ROLE_COMPANY_ADMIN : UserRole.ROLE_BROKER;

                try {
                  userService.updateUserStatus(brokerProfileResponse.getUserId(), userRole, UserStatus.ACTIVE);
                } catch (UserNotFoundException e) {
                  LOGGER.error("User {} not found", brokerProfileResponse.getUserId());
                }

                break;
              default:
                break;
            }
          });
    }
  }
}
