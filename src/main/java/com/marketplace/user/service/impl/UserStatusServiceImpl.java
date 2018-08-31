package com.marketplace.user.service.impl;

import com.marketplace.user.domain.UserStatusBO;
import com.marketplace.user.service.UserStatusService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
@Service
class UserStatusServiceImpl implements UserStatusService {

     private final UserStatusRepository userStatusRepository;

     @Override
     public Optional<UserStatusBO> findById(final String id) {
          LOGGER.info("Finding user domain by id {}", id);
          return userStatusRepository.findById(id);
     }

     @Override
     public Optional<UserStatusBO> findByName(final String userStatus) {
          LOGGER.info("Finding user domain by name {}", userStatus);
          return userStatusRepository.findByName(userStatus);
     }
}
