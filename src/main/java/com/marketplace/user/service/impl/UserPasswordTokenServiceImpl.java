package com.marketplace.user.service.impl;

import com.marketplace.user.domain.UserBO;
import com.marketplace.user.domain.UserPasswordTokenBO;
import com.marketplace.user.service.UserPasswordTokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
class UserPasswordTokenServiceImpl implements UserPasswordTokenService {

     private final UserPasswordTokenRepository userPasswordTokenRepository;

     @Override
     public void createToken(final UserBO userBO) {
          LOGGER.info("Generating a reset password token for pending {}", userBO.getId());
          final UserPasswordTokenBO userPasswordTokenBO = this.findByUserId(userBO.getId())
              .map(upt -> upt.setCreatedTs(LocalDateTime.now()))
              .map(upt -> upt.setToken(UUID.randomUUID().toString()))
              .orElse(new UserPasswordTokenBO()
                  .setUserId(userBO.getId())
                  .setToken(UUID.randomUUID().toString())
                  .setCreatedTs(LocalDateTime.now()));

          userPasswordTokenRepository.save(userPasswordTokenBO);
     }

     @Override
     public Optional<UserPasswordTokenBO> findByUserIdAndToken(final String userId, final String token) {
          LOGGER.info("Getting a reset password token for pending {}", userId);
          return userPasswordTokenRepository.findByUserIdAndToken(userId, token);
     }

     @Override
     public void delete(final UserPasswordTokenBO userPasswordTokenBO) {
          LOGGER.info("Remove reset password token for pending {} and token {}", userPasswordTokenBO.getUserId(), userPasswordTokenBO.getToken());
          userPasswordTokenRepository.delete(userPasswordTokenBO);
     }

     private Optional<UserPasswordTokenBO> findByUserId(final String userId) {
          LOGGER.info("Getting a reset password token for pending {}", userId);
          return userPasswordTokenRepository.findByUserId(userId);
     }
}
