package com.marketplace.user.queue.consume;

import com.google.gson.Gson;
import com.marketplace.user.service.UserService;
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
class UserMessageHandlerImpl implements MessageHandler {

     private final Gson gson;
     private final UserService userService;

     @Override
     public void handleMessage(final Message<?> message) throws MessagingException {
          LOGGER.debug("Message action is {} and message payload is {}", message.getHeaders().get("action"), message.getPayload());
          if (message.getPayload() instanceof String) {
               Optional.ofNullable(message.getHeaders())
                   .map(header -> header.get("action"))
                   .map(Object::toString)
                   .flatMap(UserConsumeAction::getActionFromString)
                   .ifPresent(consumedAction -> {
                        final String payload = (String) message.getPayload();

                        switch (consumedAction) {
                             case BROKER_ADDED_AS_ADMIN:
                             case BROKER_REMOVED_AS_ADMIN:
                             case BROKER_REMOVED_FROM_COMPANY:
                             case BROKER_VERIFIED:
                             default:
                                  break;
                        }
                   });
          }
     }
}
