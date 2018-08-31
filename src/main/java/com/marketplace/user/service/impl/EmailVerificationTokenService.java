package com.marketplace.user.service.impl;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.user.domain.EmailVerificationTokenBO;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.dto.TokenVerificationResponse;
import com.marketplace.user.exception.EmailVerificationTokenNotFoundException;
import com.marketplace.user.queue.publish.UserPublishService;
import com.marketplace.user.queue.publish.domain.UserPublishAction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
class EmailVerificationTokenService {

     private final EmailVerificationTokenRepository emailVerificationTokenRepository;
     private final UserPublishService publishService;

     public void createToken(final UserBO userBO) {
          LOGGER.info("Generating a reset password token for user {}", userBO.getId());
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

          publishService.sendMessage(UserPublishAction.VERIFY_EMAIL, emailVerificationResponse);
     }

     public EmailVerificationTokenBO verifyToken(final String token) throws EmailVerificationTokenNotFoundException {
          LOGGER.info("Getting a reset password token {}", token);
          final EmailVerificationTokenBO emailVerificationTokenBO = emailVerificationTokenRepository.findByToken(token)
              .orElseThrow(() -> new EmailVerificationTokenNotFoundException(token));

          if (emailVerificationTokenBO.getCreatedTs().compareTo(LocalDateTime.now().minusDays(2)) <= 0) {
               LOGGER.debug("Token generated at {} for user id {} and token {} has expired", emailVerificationTokenBO.getCreatedTs(), emailVerificationTokenBO.getUserId(), token);
               throw new BadRequestException(String.format("Email verification token [ %s ] has expired", token));
          }

          LOGGER.info("Removing email verification token {}", token);
          emailVerificationTokenRepository.delete(emailVerificationTokenBO);
          return emailVerificationTokenBO;
     }
}
