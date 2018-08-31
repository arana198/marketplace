package com.marketplace.profile.service.impl

import com.marketplace.profile.domain.UserProfileBO
import com.marketplace.profile.dto.UserProfileRequest
import com.marketplace.user.domain.RoleBO
import com.marketplace.user.dto.RoleRequest
import com.marketplace.user.service.impl.RoleRequestConverter
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

import static com.marketplace.user.dto.RoleRequest.UserRole

class UserProfileRequestConverterSpec extends Specification {

    private UserProfileRequest userProfileRequest

    @Subject
    private UserProfileRequestConverter underTest

    void setup() {

        underTest = new UserProfileRequestConverter()

        userProfileRequest = Mock()
    }

    def "it should convert successfully"() {
        given:
            String userId = "userId"
            String firstName = "firstName"
            String lastName = "lastName"
            String mobileNumber = "mobileNumber"
            LocalDate dateOfBirth = LocalDate.of(1991, 1, 1)
        when:
            UserProfileBO result = underTest.convert(userProfileRequest)
        then:
            userProfileRequest.getUserId() >> userId
            userProfileRequest.getFirstName() >> firstName
            userProfileRequest.getLastName() >> lastName
            userProfileRequest.getMobileNumber() >> mobileNumber
            userProfileRequest.getDateOfBirth() >> dateOfBirth
        and:
            result.getUserId() == userId
            result.getFirstName() == firstName
            result.getLastName() == lastName
            result.getMobileNumber() == mobileNumber
            result.getDateOfBirth() == dateOfBirth
    }

    def "it should convert new profile to old profile successfully"() {
        given:
            String userId = "userId"
            String firstName = "firstName"
            String lastName = "lastName"
            String mobileNumber = "mobileNumber"
            LocalDate dateOfBirth = LocalDate.of(1991, 1, 1)

            UserProfileBO userProfileBO = new UserProfileBO(userId: userId)
        when:
            UserProfileBO result = underTest.convert(userProfileBO, userProfileRequest)
        then:
            userProfileRequest.getUserId() >> userId
            userProfileRequest.getFirstName() >> firstName
            userProfileRequest.getLastName() >> lastName
            userProfileRequest.getMobileNumber() >> mobileNumber
            userProfileRequest.getDateOfBirth() >> dateOfBirth
        and:
            result.getUserId() == userId
            result.getFirstName() == firstName
            result.getLastName() == lastName
            result.getMobileNumber() == mobileNumber
            result.getDateOfBirth() == dateOfBirth
    }
}
