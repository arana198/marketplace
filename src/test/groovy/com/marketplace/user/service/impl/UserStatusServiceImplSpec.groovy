package com.marketplace.user.service.impl

import com.marketplace.user.domain.RoleBO
import com.marketplace.user.domain.UserStatusBO
import com.marketplace.user.dto.RoleRequest
import com.marketplace.user.exception.RoleNotFoundException
import spock.lang.Specification
import spock.lang.Subject

class UserStatusServiceImplSpec extends Specification {

    private UserStatusRepository userStatusRepository

    private UserStatusBO userStatusBO

    @Subject
    private UserStatusServiceImpl underTest

    void setup() {

        userStatusRepository = Mock()

        underTest = new UserStatusServiceImpl(
                userStatusRepository
        )

        userStatusBO = Mock()
    }

    def "it should return user status for an id"() {
        given:
            String id = "123"
        when:
            Optional<UserStatusBO> result = underTest.findById(id)
        then:
            1 * userStatusRepository.findById(id) >> Optional.of(userStatusBO)
        and:
            result.isPresent()
            result.get() == userStatusBO
    }

    def "it should return user statusfor a name"() {
        given:
            String status = "status"
        when:
            Optional<UserStatusBO> result = underTest.findByName(status)
        then:
            1 * userStatusRepository.findByName(status) >> Optional.of(userStatusBO)
        and:
            result.isPresent()
            result.get() == userStatusBO
    }
}
