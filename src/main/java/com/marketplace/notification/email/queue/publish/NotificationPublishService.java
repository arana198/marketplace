package com.marketplace.notification.email.queue.publish;

import com.marketplace.notification.email.queue.publish.domain.NotificationPublishAction;

public interface NotificationPublishService {
  void sendMessage(NotificationPublishAction action, Object object);
}