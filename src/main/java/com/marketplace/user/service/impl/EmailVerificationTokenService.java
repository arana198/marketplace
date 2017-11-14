package com.marketplace.user.service.impl;

import com.marketplace.queue.publish.PublishService;
import com.marketplace.queue.publish.domain.PublishAction;
import com.marketplace.user.domain.EmailVerificationTokenBO;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.dto.TokenVerificationResponse;
import com.marketplace.user.exception.EmailVerificationTokenNotFoundException;
import com.marketplace.user.exception.UserPasswordTokenExpiredException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Slf4j
@Service
class EmailVerificationTokenService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PublishService publishService;

    public void createToken(final UserBO userBO) {
        log.info("Generating a reset password token for user {}", userBO.getId());
        final EmailVerificationTokenBO emailVerificationTokenBO = emailVerificationTokenRepository.findByUserId(userBO.getId())
                .map(upt -> upt.setCreatedTs(LocalDateTime.now()))
                .map(upt -> upt.setToken(UUID.randomUUID().toString()))
                .orElse(new EmailVerificationTokenBO()
                        .setUserId(userBO.getId())
                        .setToken(UUID.randomUUID().toString())
                        .setCreatedTs(LocalDateTime.now()));

        emailVerificationTokenRepository.save(emailVerificationTokenBO);
        final TokenVerificationResponse emailVerificationResponse = new TokenVerificationResponse(userBO.getId(),
                userBO.getUsername(),
                emailVerificationTokenBO.getToken(),
                emailVerificationTokenBO.getCreatedTs());

        publishService.sendMessage(PublishAction.VERIFY_EMAIL, emailVerificationResponse);
    }

    public void verifyToken(final String userId, final String token) throws EmailVerificationTokenNotFoundException {
        log.info("Getting a reset password token for user {}", userId);
        final EmailVerificationTokenBO emailVerificationTokenBO = emailVerificationTokenRepository.findByUserIdAndToken(userId, token)
                .orElseThrow(() -> new EmailVerificationTokenNotFoundException(userId, token));

        if (emailVerificationTokenBO.getCreatedTs().compareTo(LocalDateTime.now().minusDays(2)) <= 0) {
            log.debug("Token generated at {} for user id {} and token {} has expired", emailVerificationTokenBO.getCreatedTs(), userId, token);
            throw new UserPasswordTokenExpiredException(userId, token, emailVerificationTokenBO.getCreatedTs().plusDays(2));
        }

        log.info("Removing email verification token {} and token {}", userId, token);
        emailVerificationTokenRepository.delete(emailVerificationTokenBO);
    }
}
