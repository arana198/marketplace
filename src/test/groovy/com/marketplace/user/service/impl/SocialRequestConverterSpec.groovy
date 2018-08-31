package com.marketplace.user.service.impl

import com.marketplace.user.domain.UserBO
import com.marketplace.user.dto.SocialUserRequest
import spock.lang.Specification
import spock.lang.Subject

class SocialRequestConverterSpec extends Specification {

    private SocialUserRequest socialUserRequest

    @Subject
    private SocialUserRequestConverter underTest

    void setup() {

        underTest = new SocialUserRequestConverter()

        socialUserRequest = Mock()
    }

    def "it should convert successfully"() {
        given:
            String email = "description"
        when:
            UserBO result = underTest.convert(socialUserRequest)
        then:
            socialUserRequest.getEmail() >> email
        and:
            result.getUsername() == email
            result.isEmailVerified() == true
    }
}
