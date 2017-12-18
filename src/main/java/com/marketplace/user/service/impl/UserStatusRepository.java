package com.marketplace.user.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.user.domain.UserStatusBO;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

interface UserStatusRepository extends BaseRepository<UserStatusBO, String> {

    @Cacheable("UserStatusRepository.findById")
    Optional<UserStatusBO> findById(String id);

    @Cacheable("UserStatusRepository.findByName")
    Optional<UserStatusBO> findByName(String userStatus);
}
