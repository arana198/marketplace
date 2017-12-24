package com.marketplace.profile.queue.publish;

import com.marketplace.profile.queue.publish.domain.ProfilePublishAction;

public interface ProfilePublishService {
    void sendMessage(ProfilePublishAction action, Object object);
}