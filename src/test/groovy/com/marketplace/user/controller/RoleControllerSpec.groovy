package com.marketplace.user.controller

import com.marketplace.common.exception.handler.DefaultExceptionHandler
import com.marketplace.user.dto.RoleRequest
import com.marketplace.user.dto.RoleResponse
import com.marketplace.user.service.RoleService
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class RoleControllerSpec extends Specification {

    static final String BASE_URL = "/roles"

    private RoleService roleService

    MockMvc mockMvc

    def setup() {
        roleService = Mock()

        mockMvc = standaloneSetup(new RoleController(roleService))
            .setControllerAdvice(new DefaultExceptionHandler())
            .build()
    }

    def "it should register a user"() {
        given:
            String roleId = "roleId"
            String name = "name"
            String description = "desc"
            Boolean isSelectable = true

            RoleResponse roleResponse = new RoleResponse(
                    roleId,
                    name,
                    description,
                    isSelectable
            )

        when:
            def response = mockMvc.perform(get("${BASE_URL}")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response

            def content = new JsonSlurper().parseText(response.contentAsString)

        then:
            1 * roleService.findActiveRoles() >> [roleResponse]

            response.status == HttpStatus.OK.value()

            content.roles.size == 1
            content.roles[0].roleId == roleId
            content.roles[0].name == name
            content.roles[0].description == description
    }

    def "it should update password"() {

        given:
            String roleId = "roleId"
            RoleRequest roleRequest = new RoleRequest(roleId, RoleRequest.UserRole.ROLE_ADMIN, "testoo", true)
        when:
            def response = mockMvc.perform(put("${BASE_URL}/roleId")
                    .content(toJson(roleRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn().response
        then:
            roleService.updateRole(roleId, roleRequest)
            response.status == HttpStatus.NO_CONTENT.value()
    }
}