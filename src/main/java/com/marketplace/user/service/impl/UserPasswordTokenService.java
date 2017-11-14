package com.marketplace.user.service.impl;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.queue.publish.PublishService;
import com.marketplace.queue.publish.domain.PublishAction;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.domain.UserPasswordTokenBO;
import com.marketplace.user.dto.TokenVerificationResponse;
import com.marketplace.user.exception.UserPasswordTokenNotFoundException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Slf4j
@Service
class UserPasswordTokenService {

    private final UserPasswordTokenRepository userPasswordTokenRepository;
    private final PublishService publishService;

    public void createToken(final UserBO userBO) {
        log.info("Generating a reset password token for user {}", userBO.getId());
        final UserPasswordTokenBO userPasswordTokenBO = userPasswordTokenRepository.findByUserId(userBO.getId())
                .map(upt -> upt.setCreatedTs(LocalDateTime.now()))
                .map(upt -> upt.setToken(UUID.randomUUID().toString()))
                .orElse(new UserPasswordTokenBO()
                        .setUserId(userBO.getId())
                        .setToken(UUID.randomUUID().toString())
                        .setCreatedTs(LocalDateTime.now()));

        userPasswordTokenRepository.save(userPasswordTokenBO);
        final TokenVerificationResponse emailVerificationResponse = new TokenVerificationResponse(userBO.getId(),
                userBO.getUsername(),
                userPasswordTokenBO.getToken(),
                userPasswordTokenBO.getCreatedTs());

        publishService.sendMessage(PublishAction.FORGOTTEN_PASSWORD, emailVerificationResponse);
    }

    public void verifyToken(final String userId, final String token) throws UserPasswordTokenNotFoundException {
        log.info("Getting a reset password token for user {}", userId);
        final UserPasswordTokenBO userPasswordToken = userPasswordTokenRepository.findByUserIdAndToken(userId, token)
                .orElseThrow(() -> new UserPasswordTokenNotFoundException(userId, token));

        if (userPasswordToken.getCreatedTs().compareTo(LocalDateTime.now().minusDays(2)) <= 0) {
            log.debug("Token generated at {} for user id {} and token {} has expired", userPasswordToken.getCreatedTs(), userId, token);
            throw new BadRequestException(String.format("User password token [ %s ] has expired", token));
        }

        log.info("Remove reset password token for user {} and token {}", userId, token);
        userPasswordTokenRepository.delete(userPasswordToken);
    }
}
