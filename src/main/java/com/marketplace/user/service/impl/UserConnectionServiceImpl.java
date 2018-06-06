package com.marketplace.user.service.impl;

import com.marketplace.user.domain.UserBO;
import com.marketplace.user.domain.UserRoleBO;
import com.marketplace.user.service.UserConnectionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@AllArgsConstructor
@Slf4j
@Component
class UserConnectionServiceImpl implements UserConnectionService {

     private final UserConnectionRepository userConnectionRepository;
     private final UserRepository userRepository;

     @Override
     public boolean checkValidityForProviderTokenByUser(final String providerId, final String username) {
          final String providerUserId = userRepository.findByUsername(username)
              .map(UserBO::getRoles)
              .filter(userRoles -> userRoles.parallelStream().anyMatch(ur -> ur.getProvider().equalsIgnoreCase(providerId)))
              .flatMap(ur -> ur.parallelStream().findAny())
              .map(UserRoleBO::getProviderUserId)
              .orElse(null);

          return userConnectionRepository.findByProviderIdAndProviderUserId(providerId, providerUserId)
              .map(uc -> LocalDateTime.ofInstant(Instant.ofEpochMilli(uc.getExpireTime()), ZoneId.systemDefault()))
              .filter(expiryTime -> expiryTime.isBefore(LocalDateTime.now()))
              .isPresent();
     }
}
