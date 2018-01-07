package com.marketplace.profile.queue.consume;

import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Data
@Slf4j
@Service
class ProfileMessageHandlerImpl implements MessageHandler {

    private final Gson gson;

    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
        log.debug("Message action is {} and message payload is {}", message.getHeaders().get("action"), message.getPayload());
        if (message.getPayload() instanceof String) {
            ProfileConsumeAction.getActionFromString(message.getHeaders().get("action").toString())
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
