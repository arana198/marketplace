package com.marketplace.profile.controller

import com.marketplace.common.exception.handler.DefaultExceptionHandler
import com.marketplace.profile.ProfileController
import com.marketplace.profile.dto.UserProfileRequest
import com.marketplace.profile.dto.UserProfileResponse
import com.marketplace.profile.service.UserProfileService
import com.marketplace.user.dto.EmailVerificationRequest
import com.marketplace.user.dto.UpdatePasswordRequest
import com.marketplace.user.dto.UserRequest
import groovy.json.JsonSlurper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDate

import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class ProfileControllerSpec extends Specification {

    static final String BASE_URL = "/users/"

    private UserProfileService userProfileService

    MockMvc mockMvc

    def setup() {
        userProfileService = Mock()

        mockMvc = standaloneSetup(new ProfileController(userProfileService))
            .setControllerAdvice(new DefaultExceptionHandler())
            .build()
    }

    def "it should get a profile for a user"() {
        given:
            String userId = "userId"
            String profileId = "profileId"
            String email = "email"
            String firstName = "firstName"
            String lastName = "lastName"
            String mobileNumber = "mobileNumber"
            String postcode = "postcode"
            LocalDate dateOfBirth = LocalDate.of(1991, 1, 1)

            UserProfileResponse userProfileResponse = new UserProfileResponse(
                    profileId,
                    userId,
                    email,
                    firstName,
                    lastName,
                    mobileNumber,
                    dateOfBirth,
                    postcode
            )

        when:
            def response = mockMvc.perform(get("${BASE_URL}/$userId/profiles")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().response

            def content = new JsonSlurper().parseText(response.contentAsString)

        then:
            1 * userProfileService.findByUserId(userId) >> Optional.of(userProfileResponse)

            response.status == HttpStatus.OK.value()
            content.profileId == profileId
            content.userId == userId
            content.firstName == firstName
            content.lastName == lastName
            content.mobileNumber == mobileNumber
            content.postcode == postcode
    }

    @Ignore
    def "it should create profile"() {

        given:
            String userId = "userId"
            String profileId = "profileId"
            String email = "email"
            String firstName = "firstName"
            String lastName = "lastName"
            String mobileNumber = "mobileNumber"
            String postcode = "postcode"
            LocalDate dateOfBirth = LocalDate.of(1991, 1, 1)

            UserProfileRequest userProfileRequest = UserProfileRequest.builder()
                    .userId(userId)
                    .firstName(firstName)
                    .lastName(lastName)
                    .mobileNumber(mobileNumber)
                    .postcode(postcode)
                    .build()

        when:
            def response = mockMvc.perform(put("${BASE_URL}/$userId/profiles")
                    .content(toJson(userProfileRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn().response
        then:
            userProfileService.createProfile(userId, userProfileRequest) >> new UserProfileResponse(profileId, null, null, null, null, null, null, null)
            response.status == HttpStatus.CREATED.value()
    }

    @Ignore
    def "it should update profile"() {

        given:
            String userId = "userId"
            String profileId = "profileId"
            String email = "email"
            String firstName = "firstName"
            String lastName = "lastName"
            String mobileNumber = "mobileNumber"
            String postcode = "postcode"
            LocalDate dateOfBirth = LocalDate.of(1991, 1, 1)

            UserProfileRequest userProfileRequest = UserProfileRequest.builder()
                    .userId(userId)
                    .firstName(firstName)
                    .lastName(lastName)
                    .mobileNumber(mobileNumber)
                    .postcode(postcode)
                    .build()

        when:
            def response = mockMvc.perform(put("${BASE_URL}/$userId/profiles/$profileId")
                    .content(toJson(userProfileRequest))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn().response
        then:
            userProfileService.updateProfile(userId, profileId, userProfileRequest)
            response.status == HttpStatus.OK.value()
    }

}