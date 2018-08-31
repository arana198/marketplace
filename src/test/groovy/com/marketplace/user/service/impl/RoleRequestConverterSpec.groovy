package com.marketplace.user.service.impl

import com.marketplace.user.domain.RoleBO
import com.marketplace.user.dto.RoleList
import com.marketplace.user.dto.RoleRequest
import com.marketplace.user.dto.RoleResponse
import com.marketplace.user.exception.RoleNotFoundException
import spock.lang.Specification
import spock.lang.Subject

import static com.marketplace.user.dto.RoleRequest.UserRole

class RoleRequestConverterSpec extends Specification {

    private RoleRequest roleRequest

    @Subject
    private RoleRequestConverter underTest

    void setup() {

        underTest = new RoleRequestConverter()

        roleRequest = Mock()
    }

    def "it should convert successfully"() {
        given:
            UserRole name = UserRole.ROLE_ADMIN
            String description = "description"
            boolean isSelectable = true
        when:
            RoleBO result = underTest.convert(roleRequest)
        then:
            roleRequest.getName() >> name
            roleRequest.getDescription() >> description
            roleRequest.isSelectable() >> isSelectable
        and:
            result.getName() == name.value
            result.getDescription() == description
            result.isSelectable() == true
    }
}
