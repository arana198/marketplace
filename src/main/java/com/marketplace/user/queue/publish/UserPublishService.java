package com.marketplace.user.queue.publish;

import com.marketplace.user.queue.publish.domain.UserPublishAction;

public interface UserPublishService {
  void sendMessage(UserPublishAction action, Object object);
}