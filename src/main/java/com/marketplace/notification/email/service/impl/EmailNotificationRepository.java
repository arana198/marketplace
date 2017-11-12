package com.marketplace.notification.email.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.notification.email.domain.EmailNotificationBO;
import org.springframework.stereotype.Repository;

@Repository
interface EmailNotificationRepository extends BaseRepository<EmailNotificationBO, String> {
}
