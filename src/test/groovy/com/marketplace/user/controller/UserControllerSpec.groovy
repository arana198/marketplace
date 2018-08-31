package com.marketplace.user.controller

import com.marketplace.common.exception.handler.DefaultExceptionHandler
import com.marketplace.user.dto.EmailVerificationRequest
import com.marketplace.user.dto.UpdatePasswordRequest
import com.marketplace.user.dto.UserRequest
import com.marketplace.user.service.UserService
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static com.marketplace.user.dto.UserRequest.UserType
import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class UserControllerSpec extends Specification {

    static final String BASE_URL = "/users"

    private UserService userService

    MockMvc mockMvc

    def setup() {
        userService = Mock()

        mockMvc = standaloneSetup(new UserController(userService))
            .setControllerAdvice(new DefaultExceptionHandler())
            .build()
    }

    def "it should register a user"() {
        given:
            String email = "test@mail.com"
            String password = "password"

            UserType userType = UserType.BROKER

            UserRequest userRequest = new UserRequest(
                    email,
                    password
            )

        when:
            def response = mockMvc.perform(post("${BASE_URL}?userType=${userType}")
                .content(toJson(userRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response

        then:
            1 * userService.createUser(userRequest, userType)

            response.status == HttpStatus.CREATED.value()
    }

    def "it should update password"() {

        given:
            String userId = "userId"
            UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest("1234225")
        when:
            def response = mockMvc.perform(put("${BASE_URL}/userId/passwords")
                    .content(toJson(updatePasswordRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn().response
        then:
            userService.updatePassword(userId, updatePasswordRequest)
            response.status == HttpStatus.OK.value()
    }

    def "it should verify email"() {

        given:
            EmailVerificationRequest emailVerificationRequest = new EmailVerificationRequest("123")
        when:
            def response = mockMvc.perform(put("${BASE_URL}/verifyemail")
                    .content(toJson(emailVerificationRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn().response
        then:
            userService.verifyEmail(emailVerificationRequest)
            response.status == HttpStatus.OK.value()
    }

}