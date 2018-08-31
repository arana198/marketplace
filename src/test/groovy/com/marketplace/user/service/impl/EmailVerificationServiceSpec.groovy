package com.marketplace.user.service.impl

import com.marketplace.common.exception.BadRequestException
import com.marketplace.user.domain.EmailVerificationTokenBO
import com.marketplace.user.domain.UserBO
import com.marketplace.user.domain.UserStatusBO
import com.marketplace.user.dto.TokenVerificationResponse
import com.marketplace.user.exception.EmailVerificationTokenNotFoundException
import com.marketplace.user.queue.publish.UserPublishService
import com.marketplace.user.queue.publish.domain.UserPublishAction
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class EmailVerificationServiceSpec extends Specification {

    private EmailVerificationTokenRepository emailVerificationTokenRepository
    private UserPublishService publishService

    private EmailVerificationTokenBO emailVerificationTokenBO

    @Subject
    private EmailVerificationTokenService underTest

    void setup() {

        emailVerificationTokenRepository = Mock()
        publishService = Mock()

        underTest = new EmailVerificationTokenService(
                emailVerificationTokenRepository,
                publishService
        )

        emailVerificationTokenBO = Mock()
    }

    def "it should create a token successfully"() {
        given:
            String userId = "123"

            UserBO userBO = Mock()
        when:
           underTest.createToken(userBO)
        then:
            userBO.getId() >> userId
            1 * emailVerificationTokenRepository.findByUserId(userId) >> Optional.of(emailVerificationTokenBO)
            1 * emailVerificationTokenRepository.save(_ as EmailVerificationTokenBO)
            1 * publishService.sendMessage(UserPublishAction.VERIFY_EMAIL, _ as TokenVerificationResponse)
    }

    def "it should return EmailVerificationTokenBO for a given token"() {
        given:
            String token = "token"
        when:
            EmailVerificationTokenBO result = underTest.verifyToken(token)
        then:
            emailVerificationTokenBO.getCreatedTs() >> LocalDateTime.now()
            1 * emailVerificationTokenRepository.findByToken(token) >> Optional.of(emailVerificationTokenBO)
            1 * emailVerificationTokenRepository.delete(emailVerificationTokenBO)
        and:
            result == emailVerificationTokenBO
    }

    def "it should throw EmailVerificationTokenNotFoundException when verification token not found for a given token"() {
        given:
            String token = "token"
        when:
            EmailVerificationTokenBO result = underTest.verifyToken(token)
        then:
            emailVerificationTokenBO.getCreatedTs() >> LocalDateTime.now()
            1 * emailVerificationTokenRepository.findByToken(token) >> Optional.empty()
            0 * emailVerificationTokenRepository.delete(emailVerificationTokenBO)
        and:
            def ex = thrown(EmailVerificationTokenNotFoundException)
            ex.message == "Email verification token [ ${token} ] not found"
    }

    def "it should throw BadRequestException when token expired"() {
        given:
            String token = "token"
        when:
            EmailVerificationTokenBO result = underTest.verifyToken(token)
        then:
            emailVerificationTokenBO.getCreatedTs() >> LocalDateTime.now().minusYears(1)
            1 * emailVerificationTokenRepository.findByToken(token) >> Optional.of(emailVerificationTokenBO)
            0 * emailVerificationTokenRepository.delete(emailVerificationTokenBO)
        and:
            def ex = thrown(BadRequestException)
            ex.message == "Email verification token [ ${token} ] has expired"
    }
}
