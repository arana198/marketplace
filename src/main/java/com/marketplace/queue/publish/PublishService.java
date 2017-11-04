package com.marketplace.queue.publish;

import com.marketplace.queue.publish.domain.PublishAction;

public interface PublishService {
    void sendMessage(PublishAction action, Object object);
}