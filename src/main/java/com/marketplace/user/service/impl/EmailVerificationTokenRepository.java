package com.marketplace.user.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.user.domain.EmailVerificationTokenBO;

import java.util.Optional;

interface EmailVerificationTokenRepository extends BaseRepository<EmailVerificationTokenBO, Long> {

    Optional<EmailVerificationTokenBO> findByToken(String token);

    Optional<EmailVerificationTokenBO> findByUserId(String userId);
}
