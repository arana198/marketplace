package com.marketplace.notification.email.queue.publish;

import com.google.gson.Gson;
import com.marketplace.notification.email.queue.publish.domain.NotificationPublishAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
class NotificationPublishServiceImpl implements NotificationPublishService {

    private static final String APPLICATION_NAME = "marketplace";

    private final MessageChannel messageChannel;

    private final Gson gson;

    NotificationPublishServiceImpl(@Qualifier("notificationMessageChannel") final MessageChannel messageChannel,
                                   final Gson gson) {
        this.messageChannel = messageChannel;
        this.gson = gson;
    }

    @Override
    public void sendMessage(final NotificationPublishAction action, final Object object) {
        log.info("Publishing with routing key {}", action);
        final String json = gson.toJson(object);

        try {
            final Map<String, Object> headers = new HashMap<String, Object>();
            headers.put("action", action.getValue());
            headers.put("applicationName", APPLICATION_NAME);
            messageChannel.send(new GenericMessage<>(json, headers));
        } catch (Exception e) {
            log.error("error sending message {}. Message: {}", json, e.getMessage());
        }
    }
}