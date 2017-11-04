package com.marketplace.queue.consume;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
class MessageHandlerImpl implements MessageHandler {
    @Override
    public void handleMessage(final Message<?> message) throws MessagingException {
        System.out.println("printing message " + message);
    }
}
