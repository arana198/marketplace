package com.marketplace.user.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.user.domain.UserConnectionBO;

import java.util.Optional;

interface UserConnectionRepository extends BaseRepository<UserConnectionBO, String> {
     Optional<UserConnectionBO> findByProviderIdAndProviderUserId(String providerId, String userId);
}
