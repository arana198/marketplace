package com.marketplace.user.service.impl

import com.marketplace.user.domain.RoleBO
import com.marketplace.user.domain.UserBO
import com.marketplace.user.domain.UserRoleBO
import com.marketplace.user.dto.RoleResponse
import com.marketplace.user.dto.UserResponse
import com.marketplace.user.dto.UserRoleResponse
import spock.lang.Specification
import spock.lang.Subject

class UserResponseConverterSpec extends Specification {

    private UserRoleResponseConverter userRoleResponseConverter

    private UserBO userBO

    @Subject
    private UserResponseConverter underTest

    void setup() {

        userRoleResponseConverter = Mock()
        underTest = new UserResponseConverter(userRoleResponseConverter)

        userBO = Mock()
    }

    def "it should convert successfully"() {
        given:
            String userId = "userId"
            String email = "email"
            String description = "description"
            boolean isEmailVerified = true

            UserRoleResponse userRoleResponse = Mock()
            UserRoleBO userRoleBO = Mock()
        when:
            UserResponse result = underTest.convert(userBO)
        then:
            userBO.getId() >> userId
            userBO.getUsername() >> email
            userBO.getRoles() >> [userRoleBO]
            userBO.isEmailVerified() >> isEmailVerified
            1 * userRoleResponseConverter.convert(new HashSet<UserRoleBO>([userRoleBO])) >> [userRoleResponse]
        and:
            result.getUserId() == userId
            result.getEmail() == email
            result.getUserRoles() == [userRoleResponse]
            result.isEmailVerified() == true
    }
}
