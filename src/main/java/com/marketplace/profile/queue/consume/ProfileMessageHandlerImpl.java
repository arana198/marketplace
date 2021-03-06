package com.marketplace.profile.queue.consume;

import com.google.gson.Gson;
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
class ProfileMessageHandlerImpl implements MessageHandler {

     private final Gson gson;

     @Override
     public void handleMessage(final Message<?> message) throws MessagingException {
          LOGGER.debug("Message action is {} and message payload is {}", message.getHeaders().get("action"), message.getPayload());
          if (message.getPayload() instanceof String) {
               Optional.ofNullable(message.getHeaders())
                   .map(header -> header.get("action"))
                   .map(Object::toString)
                   .flatMap(ProfileConsumeAction::getActionFromString)
                   .ifPresent(consumedAction -> {
                        final String payload = (String) message.getPayload();

                        switch (consumedAction) {
                             default:
                                  break;
                        }
                   });
          }
     }
}
