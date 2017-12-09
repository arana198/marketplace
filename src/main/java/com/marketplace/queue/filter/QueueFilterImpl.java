package com.marketplace.queue.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSelector;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class QueueFilterImpl implements MessageSelector {

    @ServiceActivator
    @Override
    public boolean accept(Message<?> message) {
        final Object applicationName = message.getHeaders().get("applicationName");
        log.info("Received message from application [ {} ] with action [ {} ]", applicationName, message.getHeaders().get("action"));
        /**if ("company-service".equals(applicationName)) {
            return true;
        }*/
        return true;
    }

}
