package com.marketplace.profile.service.impl;

import com.marketplace.profile.domain.UserProfileBO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserProfileRepository extends JpaRepository<UserProfileBO, String> {
    Optional<UserProfileBO> findByUserId(String userId);

    Optional<UserProfileBO> findByEmail(String email);

    Optional<UserProfileBO> findByIdAndUserId(String id, String userId);

    Optional<UserProfileBO> findByMobileNumber(String mobileNumber);
}
