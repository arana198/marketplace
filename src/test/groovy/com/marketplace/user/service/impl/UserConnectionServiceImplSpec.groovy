package com.marketplace.user.service.impl

import com.marketplace.user.domain.RoleBO
import com.marketplace.user.domain.UserBO
import com.marketplace.user.domain.UserConnectionBO
import com.marketplace.user.domain.UserRoleBO
import com.marketplace.user.dto.RoleList
import com.marketplace.user.dto.RoleRequest
import com.marketplace.user.dto.RoleResponse
import com.marketplace.user.exception.RoleNotFoundException
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.TemporalField

import static com.marketplace.user.dto.RoleRequest.UserRole

class UserConnectionServiceImplSpec extends Specification {

    private UserConnectionRepository userConnectionRepository
    private UserRepository userRepository

    private UserBO userBO

    @Subject
    private UserConnectionServiceImpl underTest

    void setup() {

        userConnectionRepository = Mock()
        userRepository = Mock()

        underTest = new UserConnectionServiceImpl(
                userConnectionRepository,
                userRepository
        )

        userBO = Mock()
    }

    def "it should return true when provider token for User is valid"() {
        given:
            String providerId = "facebook"
            String username = "username"
            String providerUserId = "providerUserId"

            UserRoleBO userRoleBO = Mock()
            UserConnectionBO userConnectionBO = Mock()
        when:
            boolean result = underTest.checkValidityForProviderTokenByUser(providerId, username)
        then:
            userBO.getRoles() >> [userRoleBO]
            userRoleBO.getProvider() >> providerId
            userRoleBO.getProviderUserId() >> providerUserId
            userConnectionBO.getExpireTime() >> LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            1 * userRepository.findByUsername(username) >> Optional.of(userBO)
            1 * userConnectionRepository.findByProviderIdAndProviderUserId(providerId, providerUserId) >> Optional.of(userConnectionBO)
        and:
            result == true
    }

    def "it should return alse when provider token for User is invalid or expired"() {
        given:
            String providerId = "facebook"
            String username = "username"
            String providerUserId = "providerUserId"

            UserRoleBO userRoleBO = Mock()
            UserConnectionBO userConnectionBO = Mock()
        when:
            boolean result = underTest.checkValidityForProviderTokenByUser(providerId, username)
        then:
            userBO.getRoles() >> [userRoleBO]
            userRoleBO.getProvider() >> providerId
            userRoleBO.getProviderUserId() >> providerUserId
            userConnectionBO.getExpireTime() >> LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            1 * userRepository.findByUsername(username) >> Optional.of(userBO)
            1 * userConnectionRepository.findByProviderIdAndProviderUserId(providerId, providerUserId) >> Optional.of(userConnectionBO)
        and:
            result == false
    }
}
