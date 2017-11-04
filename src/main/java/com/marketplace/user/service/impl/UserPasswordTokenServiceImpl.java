package com.marketplace.user.service.impl;

import com.marketplace.user.domain.UserPasswordTokenBO;
import com.marketplace.user.service.UserPasswordTokenService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Slf4j
@Service
class UserPasswordTokenServiceImpl implements UserPasswordTokenService {

    private final UserPasswordTokenRepository userPasswordTokenRepository;

    @Override
    public void save(final UserPasswordTokenBO userPasswordTokenBO) {
        log.info("Generating a reset password token for pending {}", userPasswordTokenBO.getUser().getId());
        userPasswordTokenRepository.save(userPasswordTokenBO);
    }

    @Override
    public UserPasswordTokenBO findByUserId(final String userId) {
        log.info("Getting a reset password token for pending {}", userId);
        return userPasswordTokenRepository.findByUserId(userId)
                .orElse(new UserPasswordTokenBO());
    }

    @Override
    public Optional<UserPasswordTokenBO> findByUserIdAndToken(final String userId, final String token) {
        log.info("Getting a reset password token for pending {}", userId);
        return userPasswordTokenRepository.findByUserIdAndToken(userId, token);
    }

    @Override
    public void delete(final UserPasswordTokenBO userPasswordTokenBO) {
        log.info("Remove reset password token for pending {} and token {}", userPasswordTokenBO.getUser().getId(), userPasswordTokenBO.getToken());
        userPasswordTokenRepository.delete(userPasswordTokenBO);
    }
}
