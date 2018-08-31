package com.marketplace.profile.service.impl

import com.marketplace.profile.domain.UserProfileBO
import com.marketplace.profile.dto.UserProfileResponse
import spock.lang.Specification
import spock.lang.Subject

class UserProfileResponseConverterSpec extends Specification {

    private UserProfileBO userProfileBO

    @Subject
    private UserProfileResponseConverter underTest

    void setup() {

        underTest = new UserProfileResponseConverter()

        userProfileBO = Mock()
    }

    def "it should convert successfully"() {
        given:
            String userId = "userId"
            String email = "email"
            String firstName = "firstName"
            String lastName = "lastName"
            String mobileNumber = "mobileNumber"
            String postcode = "postcode"
        when:
            UserProfileResponse result = underTest.convert(userProfileBO)
        then:
            userProfileBO.getUserId() >> userId
            userProfileBO.getEmail() >> email
            userProfileBO.getFirstName() >> firstName
            userProfileBO.getLastName() >> lastName
            userProfileBO.getMobileNumber() >> mobileNumber
            userProfileBO.getPostcode() >> postcode
        and:
            result.getUserId() == userId
            result.getEmail() == email
            result.getFirstName() == firstName
            result.getLastName() == lastName
            result.getMobileNumber() == mobileNumber
            result.getPostcode() == postcode
    }
}
