package com.marketplace.user.service.impl

import com.marketplace.user.domain.RoleBO
import com.marketplace.user.domain.UserRoleBO
import com.marketplace.user.domain.UserStatusBO
import com.marketplace.user.dto.RoleResponse
import com.marketplace.user.dto.UserRoleResponse
import spock.lang.Specification
import spock.lang.Subject

class UserRoleResponseConverterSpec extends Specification {

    private RoleResponseConverter roleResponseConverter

    private UserRoleBO userRoleBO

    @Subject
    private UserRoleResponseConverter underTest

    void setup() {

        roleResponseConverter = Mock()
        underTest = new UserRoleResponseConverter(roleResponseConverter)

        userRoleBO = Mock()
    }

    def "it should convert successfully"() {
        given:
            RoleBO roleBO = Mock()
            UserStatusBO userStatusBO = Mock()

            String roleName = "roleName"
            String userStatusName = "userStatusName"
        when:
            UserRoleResponse result = underTest.convert(userRoleBO)
        then:
            userRoleBO.getRole() >> roleBO
            userRoleBO.getUserStatus() >> userStatusBO
            userStatusBO.getName() >> userStatusName
            roleBO.getName() >> roleName
        and:
            result.getUserStatus() == userStatusName
            result.getRole() == roleName
    }
}
