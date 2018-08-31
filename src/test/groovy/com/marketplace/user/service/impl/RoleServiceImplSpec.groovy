package com.marketplace.user.service.impl

import com.marketplace.user.domain.RoleBO
import com.marketplace.user.dto.RoleList
import com.marketplace.user.dto.RoleRequest
import com.marketplace.user.dto.RoleResponse
import com.marketplace.user.exception.RoleNotFoundException
import spock.lang.Specification
import spock.lang.Subject

import static com.marketplace.user.dto.RoleRequest.UserRole

class RoleServiceImplSpec extends Specification {

    private RoleRepository roleRepository
    private RoleResponseConverter roleResponseConverter
    private RoleRequestConverter roleRequestConverter

    private RoleBO roleBO

    @Subject
    private RoleServiceImpl underTest

    void setup() {

        roleRepository = Mock()
        roleResponseConverter = Mock()
        roleRequestConverter = Mock()

        underTest = new RoleServiceImpl(
                roleRepository,
                roleResponseConverter,
                roleRequestConverter
        )

        roleBO = Mock()
    }

    def "it should return active roles"() {
        given:
            RoleResponse roleResponse = Mock()
        when:
            RoleList result = underTest.findActiveRoles()
        then:
            1 * roleRepository.findBySelectable(true) >> [roleBO]
            1 * roleResponseConverter.convert(roleBO) >> roleResponse
        and:
            result.getRoles().size() == 1
            result.getRoles().get(0) == roleResponse
    }

    def "it should return roles for an id"() {
        given:
            String id = "123"
        when:
            Optional<RoleBO> result = underTest.findById(id)
        then:
            1 * roleRepository.findById(id) >> Optional.of(roleBO)
        and:
            result.isPresent()
            result.get() == roleBO
    }

    def "it should return roles for a name"() {
        given:
            UserRole name = UserRole.ROLE_ADMIN
        when:
            Optional<RoleBO> result = underTest.findByName(name)
        then:
            1 * roleRepository.findByName(name.value) >> Optional.of(roleBO)
        and:
            result.isPresent()
            result.get() == roleBO
    }

    def "it should update successfully"() {
        given:
            String roleId = "123"
            String roleName = "name"
            RoleRequest roleRequest = Mock()
            RoleBO newRole = new RoleBO()
        when:
            underTest.updateRole(roleId, roleRequest)
        then:
            roleBO.getName() >> roleName
            1 * roleRepository.findById(roleId) >> Optional.of(roleBO)
            1 * roleRequestConverter.convert(roleRequest) >> newRole
            1 * roleRepository.save(newRole)
        and:
            newRole.id == roleId
            newRole.name == roleName

    }

    def "it should throw RoleNotFoundException when updating role that does not exists"() {
        given:
            String roleId = "123"
            String roleName = "name"
            RoleRequest roleRequest = Mock()
            RoleBO newRole = new RoleBO()
        when:
            underTest.updateRole(roleId, roleRequest)
        then:
            roleBO.getName() >> roleName
            1 * roleRepository.findById(roleId) >> Optional.empty()
            0 * roleRequestConverter.convert(roleRequest) >> newRole
            0 * roleRepository.save(newRole)
        and:
            def ex = thrown(RoleNotFoundException)
            ex.message == "RoleResponse [ ${roleId} ] not found"

    }
}
