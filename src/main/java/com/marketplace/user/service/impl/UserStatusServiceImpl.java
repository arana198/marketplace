package com.marketplace.user.service.impl;

import com.marketplace.user.domain.UserStatusBO;
import com.marketplace.user.service.UserStatusService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Slf4j
@Service
class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    @Override
    public Optional<UserStatusBO> findById(final String id) {
        log.info("Finding user domain by id {}", id);
        return Optional.ofNullable(userStatusRepository.getOne(id));
    }

    @Override
    public Optional<UserStatusBO> findByName(final String userStatus) {
        log.info("Finding user domain by name {}", userStatus);
        return userStatusRepository.findByName(userStatus);
    }
}
