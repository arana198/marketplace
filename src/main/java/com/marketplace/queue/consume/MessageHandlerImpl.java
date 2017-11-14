package com.marketplace.queue.consume;

import com.google.gson.Gson;
import com.marketplace.user.dto.TokenVerificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class MessageHandlerImpl implements MessageHandler {

    private final Gson gson = new Gson();

    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
        log.debug("Message action is {} and message payload is {}", message.getHeaders().get("action"), message.getPayload());
        if (message.getPayload() instanceof String) {
            final ConsumeAction consumedAction = ConsumeAction.getActionFromString(message.getHeaders().get("action").toString());
            final String payload = (String) message.getPayload();

            switch (consumedAction) {
                case VERIFY_EMAIL:
                    TokenVerificationResponse tokenVerificationResponse = gson.fromJson(payload, TokenVerificationResponse.class);
                    break;
                case FORGOTTEN_PASSWORD:
                    tokenVerificationResponse = gson.fromJson(payload, TokenVerificationResponse.class);
                    break;
                default:
                    break;
            }
        }
    }
}
