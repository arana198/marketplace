package com.marketplace.user.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.user.domain.UserPasswordTokenBO;

import java.util.Optional;

interface UserPasswordTokenRepository extends BaseRepository<UserPasswordTokenBO, Long> {

  Optional<UserPasswordTokenBO> findByUserIdAndToken(String userId, String token);

  Optional<UserPasswordTokenBO> findByUserId(String userId);
}
