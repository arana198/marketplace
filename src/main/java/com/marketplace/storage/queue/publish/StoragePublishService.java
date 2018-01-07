package com.marketplace.storage.queue.publish;

import com.marketplace.storage.queue.publish.domain.StoragePublishAction;

public interface StoragePublishService {
    void sendMessage(StoragePublishAction action, Object object);
}