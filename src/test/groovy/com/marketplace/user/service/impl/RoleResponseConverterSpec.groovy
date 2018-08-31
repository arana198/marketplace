package com.marketplace.user.service.impl

import com.marketplace.user.domain.RoleBO
import com.marketplace.user.dto.RoleRequest
import com.marketplace.user.dto.RoleResponse
import spock.lang.Specification
import spock.lang.Subject

import static com.marketplace.user.dto.RoleRequest.UserRole

class RoleResponseConverterSpec extends Specification {

    private RoleBO roleBO

    @Subject
    private RoleResponseConverter underTest

    void setup() {

        underTest = new RoleResponseConverter()

        roleBO = Mock()
    }

    def "it should convert successfully"() {
        given:
            String roleId = "roleId"
            String name = "name"
            String description = "description"
            boolean isSelectable = true
        when:
            RoleResponse result = underTest.convert(roleBO)
        then:
            roleBO.getId() >> roleId
            roleBO.getName() >> name
            roleBO.getDescription() >> description
            roleBO.isSelectable() >> isSelectable
        and:
            result.getRoleId() == roleId
            result.getName() == name
            result.getDescription() == description
            result.isSelectable() == true
    }
}
