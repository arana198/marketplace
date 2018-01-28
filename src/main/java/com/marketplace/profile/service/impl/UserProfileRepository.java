package com.marketplace.profile.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.profile.domain.UserProfileBO;

import java.util.Optional;

interface UserProfileRepository extends BaseRepository<UserProfileBO, String> {
    Optional<UserProfileBO> findByUserId(String userId);

    Optional<UserProfileBO> findByEmail(String email);

    Optional<UserProfileBO> findByIdAndUserId(String id, String userId);

    Optional<UserProfileBO> findByMobileNumber(String mobileNumber);
}
