package com.marketplace.user.controller

import com.marketplace.common.exception.handler.DefaultExceptionHandler
import com.marketplace.user.dto.ForgottenPasswordRequest
import com.marketplace.user.dto.RoleRequest
import com.marketplace.user.dto.RoleResponse
import com.marketplace.user.service.UserService
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class ResetPasswordControllerSpec extends Specification {

    static final String BASE_URL = "/resetpassword"

    private UserService userService

    MockMvc mockMvc

    def setup() {
        userService = Mock()

        mockMvc = standaloneSetup(new ResetPasswordController(userService))
            .setControllerAdvice(new DefaultExceptionHandler())
            .build()
    }

    def "it should create a reset password token for a user"() {
        given:
            String email = "email"

        when:
            def response = mockMvc.perform(post("$BASE_URL?email=${email}")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response

        then:
            1 * userService.resetPassword(email)
            response.status == HttpStatus.CREATED.value()
    }

    def "it should update password"() {

        given:
            String email = "email@mail.com"
            String password = "password"
            String token = "token"

            ForgottenPasswordRequest forgottenPasswordRequest = new ForgottenPasswordRequest(
                    email,
                    password,
                    token
            )
        when:
            def response = mockMvc.perform(put(BASE_URL)
                    .content(toJson(forgottenPasswordRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn().response
        then:
            userService.resetPassword(forgottenPasswordRequest)
            response.status == HttpStatus.OK.value()
    }
}