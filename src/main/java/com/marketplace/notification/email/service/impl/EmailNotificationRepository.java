package com.marketplace.notification.email.service.impl;

import com.marketplace.common.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
interface EmailNotificationRepository extends BaseRepository<EmailNotificationBO, String> {
}
