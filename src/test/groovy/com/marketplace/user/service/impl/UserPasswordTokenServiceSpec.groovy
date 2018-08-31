package com.marketplace.user.service.impl

import com.marketplace.common.exception.BadRequestException
import com.marketplace.user.domain.EmailVerificationTokenBO
import com.marketplace.user.domain.UserBO
import com.marketplace.user.domain.UserPasswordTokenBO
import com.marketplace.user.dto.TokenVerificationResponse
import com.marketplace.user.exception.EmailVerificationTokenNotFoundException
import com.marketplace.user.exception.UserPasswordTokenNotFoundException
import com.marketplace.user.queue.publish.UserPublishService
import com.marketplace.user.queue.publish.domain.UserPublishAction
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime

class UserPasswordTokenServiceSpec extends Specification {

    private UserPasswordTokenRepository userPasswordTokenRepository
    private UserPublishService publishService

    private UserPasswordTokenBO userPasswordTokenBO

    @Subject
    private UserPasswordTokenService underTest

    void setup() {

        userPasswordTokenRepository = Mock()
        publishService = Mock()

        underTest = new UserPasswordTokenService(
                userPasswordTokenRepository,
                publishService
        )

        userPasswordTokenBO = Mock()
    }

    def "it should create a token successfully"() {
        given:
            String userId = "123"

            UserBO userBO = Mock()
        when:
           underTest.createToken(userBO)
        then:
            userBO.getId() >> userId
            1 * userPasswordTokenRepository.findByUserId(userId) >> Optional.of(userPasswordTokenBO)
            1 * userPasswordTokenRepository.save(_ as UserPasswordTokenBO)
            1 * publishService.sendMessage(UserPublishAction.FORGOTTEN_PASSWORD, _ as TokenVerificationResponse)
    }

    def "it should return UserPasswordTokenBO for a given token"() {
        given:
            String userId = "123"
            String token = "token"
        when:
            underTest.verifyToken(userId, token)
        then:
            userPasswordTokenBO.getCreatedTs() >> LocalDateTime.now()
            1 * userPasswordTokenRepository.findByUserIdAndToken(userId, token) >> Optional.of(userPasswordTokenBO)
            1 * userPasswordTokenRepository.delete(userPasswordTokenBO)
    }

    def "it should throw UserPasswordTokenNotFoundException when verification token not found for a given token"() {
        given:
            String userId = "123"
            String token = "token"
        when:
            underTest.verifyToken(userId, token)
        then:
            userPasswordTokenBO.getCreatedTs() >> LocalDateTime.now()
            1 * userPasswordTokenRepository.findByUserIdAndToken(userId, token) >> Optional.empty()
            0 * userPasswordTokenRepository.delete(userPasswordTokenBO)
        and:
            def ex = thrown(UserPasswordTokenNotFoundException)
            ex.message == "Password token for user [ ${userId} ] and token [ ${token} ] not found"
    }

    def "it should throw BadRequestException when token expired"() {
        given:
            String userId = "userId"
            String token = "token"
        when:
            underTest.verifyToken(userId, token)
        then:
            userPasswordTokenBO.getCreatedTs() >> LocalDateTime.now().minusYears(1)
            1 * userPasswordTokenRepository.findByUserIdAndToken(userId, token) >> Optional.of(userPasswordTokenBO)
            0 * userPasswordTokenRepository.delete(userPasswordTokenBO)
        and:
            def ex = thrown(BadRequestException)
            ex.message == "User password token [ ${token} ] has expired"
    }
}
