package com.marketplace.profile.service.impl

import com.marketplace.common.exception.BadRequestException
import com.marketplace.profile.domain.UserProfileBO
import com.marketplace.profile.dto.UserProfileRequest
import com.marketplace.profile.dto.UserProfileResponse
import com.marketplace.profile.exception.UserProfileAlreadyExistsException
import com.marketplace.profile.exception.UserProfileNotFoundException
import com.marketplace.profile.queue.publish.ProfilePublishService
import com.marketplace.profile.queue.publish.domain.ProfilePublishAction
import com.marketplace.user.domain.EmailVerificationTokenBO
import com.marketplace.user.domain.UserBO
import com.marketplace.user.dto.EmailVerificationRequest
import com.marketplace.user.dto.ForgottenPasswordRequest
import com.marketplace.user.dto.UpdatePasswordRequest
import com.marketplace.user.dto.UserResponse
import com.marketplace.user.exception.UserNotFoundException
import com.marketplace.user.queue.publish.domain.UserPublishAction
import com.marketplace.user.service.UserService
import spock.lang.Specification
import spock.lang.Subject

import java.security.Principal

class UserProfileServiceImplSpec extends Specification {

    private UserProfileResponseConverter userProfileResponseConverter
    private UserProfileRepository userProfileRepository
    private UserProfileRequestConverter userProfileRequestConverter
    private UserService userService
    private ProfilePublishService publishService

    private UserProfileBO userProfileBO

    @Subject
    private UserProfileServiceImpl underTest

    void setup() {

        userProfileResponseConverter = Mock()
        userProfileRepository = Mock()
        userProfileRequestConverter = Mock()
        userService = Mock()
        publishService = Mock()

        underTest = new UserProfileServiceImpl(
                userProfileResponseConverter,
                userProfileRepository,
                userProfileRequestConverter,
                userService,
                publishService,
        )

        userProfileBO = Mock()
    }

    def "it should create and return userprofile for a user id"() {
        given:
            String userId = "123"
            UserProfileRequest userProfileRequest = Mock()
            UserResponse userResponse = Mock()
            UserProfileResponse userProfileResponse = Mock()
        when:
            UserProfileResponse result = underTest.createProfile(userId, userProfileRequest)
        then:
            userProfileRequest.getUserId() >> userId
            1 * userProfileRepository.findByUserId(userId) >> Optional.empty()
            1 * userService.findById(userId) >> Optional.of(userResponse)
            1 * userProfileRequestConverter.convert(userProfileRequest) >> userProfileBO
            1 * userProfileRepository.save(userProfileBO)
            1 * userProfileResponseConverter.convert(userProfileBO) >> userProfileResponse
            1 * publishService.sendMessage(ProfilePublishAction.USER_PROFILE_CREATED, userProfileResponse)
        and:
            result == userProfileResponse
    }

    def "it should throw BadRequestException when creating profiling and user id does not match"() {
        given:
            String userId = "123"
            UserProfileRequest userProfileRequest = Mock()
            UserResponse userResponse = Mock()
            UserProfileResponse userProfileResponse = Mock()
        when:
            UserProfileResponse result = underTest.createProfile(userId, userProfileRequest)
        then:
            0 * userProfileRepository.findByUserId(userId) >> Optional.empty()
            0 * userService.findById(userId) >> Optional.of(userResponse)
            0 * userProfileRequestConverter.convert(userProfileRequest) >> userProfileBO
            0 * userProfileRepository.save(userProfileBO)
            0 * userProfileResponseConverter.convert(userProfileBO) >> userProfileResponse
            0 * publishService.sendMessage(ProfilePublishAction.USER_PROFILE_CREATED, userProfileResponse)
        and:
            def ex = thrown(BadRequestException)
            ex.message == "User id does not match body's user id"
    }

    def "it should throw UserProfileAlreadyExistsException when creating profiling and profile already exits"() {
        given:
            String userId = "123"
            UserProfileRequest userProfileRequest = Mock()
            UserResponse userResponse = Mock()
            UserProfileResponse userProfileResponse = Mock()
        when:
            UserProfileResponse result = underTest.createProfile(userId, userProfileRequest)
        then:
            userProfileRequest.getUserId() >> userId
            1 * userProfileRepository.findByUserId(userId) >> Optional.of(userProfileBO)
            0 * userService.findById(userId) >> Optional.of(userResponse)
            0 * userProfileRequestConverter.convert(userProfileRequest) >> userProfileBO
            0 * userProfileRepository.save(userProfileBO)
            0 * userProfileResponseConverter.convert(userProfileBO) >> userProfileResponse
            0 * publishService.sendMessage(ProfilePublishAction.USER_PROFILE_CREATED, userProfileResponse)
        and:
            def ex = thrown(UserProfileAlreadyExistsException)
            ex.message == "User company for user [ ${userId} ] already exists"
    }

    def "it should throw UserNotFoundException when creating profiling and profile already exits"() {
        given:
            String userId = "123"
            UserProfileRequest userProfileRequest = Mock()
            UserResponse userResponse = Mock()
            UserProfileResponse userProfileResponse = Mock()
        when:
            UserProfileResponse result = underTest.createProfile(userId, userProfileRequest)
        then:
            userProfileRequest.getUserId() >> userId
            1 * userProfileRepository.findByUserId(userId) >> Optional.empty()
            1 * userService.findById(userId) >> Optional.empty()
            0 * userProfileRequestConverter.convert(userProfileRequest) >> userProfileBO
            0 * userProfileRepository.save(userProfileBO)
            0 * userProfileResponseConverter.convert(userProfileBO) >> userProfileResponse
            0 * publishService.sendMessage(ProfilePublishAction.USER_PROFILE_CREATED, userProfileResponse)
        and:
            def ex = thrown(UserNotFoundException)
            ex.message == "User [ $userId ] not found"
    }

    def "it should update userprofile for a user id and profile id"() {
        given:
            String userId = "123"
            String profileId = "aad"

            UserProfileRequest userProfileRequest = Mock()
            UserResponse userResponse = Mock()
            UserProfileResponse userProfileResponse = Mock()
        when:
           underTest.updateProfile(userId, profileId, userProfileRequest)
        then:
            userProfileRequest.getUserId() >> userId
            1 * userProfileRepository.findByIdAndUserId(profileId, userId) >> Optional.of(userProfileBO)
            1 * userProfileRequestConverter.convert(userProfileBO, userProfileRequest) >> userProfileBO
            1 * userProfileRepository.save(userProfileBO)
            1 * userProfileResponseConverter.convert(userProfileBO) >> userProfileResponse
            1 * publishService.sendMessage(ProfilePublishAction.USER_PROFILE_UPDATED, userProfileResponse)
    }

    def "it should throw BadRequestException when update userprofile user id does nto match"() {
        given:
            String userId = "123"
            String profileId = "aad"

            UserProfileRequest userProfileRequest = Mock()
            UserResponse userResponse = Mock()
            UserProfileResponse userProfileResponse = Mock()
        when:
            underTest.updateProfile(userId, profileId, userProfileRequest)
        then:
            0 * userProfileRepository.findByIdAndUserId(profileId, userId) >> Optional.of(userProfileBO)
            0 * userProfileRequestConverter.convert(userProfileBO, userProfileRequest) >> userProfileBO
            0 * userProfileRepository.save(userProfileBO)
            0 * userProfileResponseConverter.convert(userProfileBO) >> userProfileResponse
            0 * publishService.sendMessage(ProfilePublishAction.USER_PROFILE_UPDATED, userProfileResponse)
        and:
            def ex = thrown(BadRequestException)
            ex.message == "User id does not match body's user id"
    }

    def "it should throw UserProfileNotFoundException when update userprofile and user id does noy found"() {
        given:
            String userId = "123"
            String profileId = "aad"

            UserProfileRequest userProfileRequest = Mock()
            UserResponse userResponse = Mock()
            UserProfileResponse userProfileResponse = Mock()
        when:
            underTest.updateProfile(userId, profileId, userProfileRequest)
        then:
            userProfileRequest.getUserId() >> userId
            1 * userProfileRepository.findByIdAndUserId(profileId, userId) >> Optional.empty()
            0 * userProfileRequestConverter.convert(userProfileBO, userProfileRequest) >> userProfileBO
            0 * userProfileRepository.save(userProfileBO)
            0 * userProfileResponseConverter.convert(userProfileBO) >> userProfileResponse
            0 * publishService.sendMessage(ProfilePublishAction.USER_PROFILE_UPDATED, userProfileResponse)
        and:
            def ex = thrown(UserProfileNotFoundException)
            ex.message == "User profile for user [ $userId ] and [ $profileId ] not found"
    }
}
