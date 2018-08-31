package com.marketplace.user.service.impl

import com.marketplace.user.domain.RoleBO
import com.marketplace.user.domain.UserBO
import com.marketplace.user.dto.RoleRequest
import com.marketplace.user.dto.UserRequest
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Subject

import static com.marketplace.user.dto.RoleRequest.UserRole

class UserRequestConverterSpec extends Specification {

    private PasswordEncoder passwordEncoder

    private UserRequest userRequest

    @Subject
    private UserRequestConverter underTest

    void setup() {

        passwordEncoder = Mock()

        underTest = new UserRequestConverter(passwordEncoder)

        userRequest = Mock()
    }

    def "it should convert successfully"() {
        given:
            String email = "description"
            String password = "password"
            String encodedPassword = "242sfgssg"
        when:
            UserBO result = underTest.convert(userRequest)
        then:
            userRequest.getEmail() >> email
            userRequest.getPassword() >> password
            1 * passwordEncoder.encode(password) >> encodedPassword
        and:
            result.getUsername() == email
            result.getPassword() == encodedPassword
    }
}
