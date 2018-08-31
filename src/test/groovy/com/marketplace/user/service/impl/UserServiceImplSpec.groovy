package com.marketplace.user.service.impl

import com.marketplace.user.domain.EmailVerificationTokenBO
import com.marketplace.user.domain.RoleBO
import com.marketplace.user.domain.UserBO
import com.marketplace.user.dto.EmailVerificationRequest
import com.marketplace.user.dto.ForgottenPasswordRequest
import com.marketplace.user.dto.RoleList
import com.marketplace.user.dto.RoleRequest
import com.marketplace.user.dto.RoleResponse
import com.marketplace.user.dto.UpdatePasswordRequest
import com.marketplace.user.dto.UserResponse
import com.marketplace.user.exception.RoleNotFoundException
import com.marketplace.user.exception.UserNotFoundException
import com.marketplace.user.oauth.TokenRevoker
import com.marketplace.user.queue.publish.UserPublishService
import com.marketplace.user.queue.publish.domain.UserPublishAction
import com.marketplace.user.service.RoleService
import com.marketplace.user.service.UserStatusService
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification
import spock.lang.Subject

import java.security.Principal

import static com.marketplace.user.dto.RoleRequest.UserRole

class UserServiceImplSpec extends Specification {

    private UserRepository userRepository
    private RoleService roleService
    private UserStatusService userStatusService
    private UserPasswordTokenService userPasswordTokenService
    private EmailVerificationTokenService emailVerificationTokenService
    private TokenRevoker tokenRevoker
    private UserResponseConverter userResponseConverter
    private UserRequestConverter userRequestConverter
    private SocialUserRequestConverter socialUserRequestConverter
    private UserPublishService publishService
    private PasswordEncoder passwordEncoder

    private UserBO userBO

    @Subject
    private UserServiceImpl underTest

    void setup() {

        userRepository = Mock()
        roleService = Mock()
        userStatusService = Mock()
        userPasswordTokenService = Mock()
        emailVerificationTokenService = Mock()
        tokenRevoker = Mock()
        userResponseConverter = Mock()
        userRequestConverter = Mock()
        socialUserRequestConverter = Mock()
        publishService = Mock()
        passwordEncoder = Mock()

        underTest = new UserServiceImpl(
                userRepository,
                roleService,
                userStatusService,
                userPasswordTokenService,
                emailVerificationTokenService,
                tokenRevoker,
                userResponseConverter,
                userRequestConverter,
                socialUserRequestConverter,
                publishService,
                passwordEncoder
        )

        userBO = Mock()
    }

    def "it should return user for a username"() {
        given:
            String username = "123"
            UserResponse userResponse = Mock()
        when:
            Optional<UserBO> result = underTest.findByUsername(username)
        then:
            1 * userRepository.findByUsername(username) >> Optional.of(userBO)
            1 * userResponseConverter.convert(userBO) >> userResponse
        and:
            result.isPresent()
            result.get() == userResponse
    }

    def "it should return user for an id"() {
        given:
            String id = "123"
            UserResponse userResponse = Mock()
        when:
            Optional<UserBO> result = underTest.findById(id)
        then:
            1 * userRepository.findById(id) >> Optional.of(userBO)
            1 * userResponseConverter.convert(userBO) >> userResponse
        and:
            result.isPresent()
            result.get() == userResponse
    }


    def "it should logout a user"() {
        given:
            String userId = "123"
            Principal principal = Mock()
        when:
            underTest.logout(principal)
        then:
            principal.getName() >> userId
            1 * tokenRevoker.revoke(userId)
    }

    def "it should update password for a user"() {
        given:
            String id = "123"
            String normalTextPassword = "password"
            String encodedPassword = '24$36fQ$6'

            UserBO oldUser = new UserBO()
            UpdatePasswordRequest userPasswordRequest = Mock()
        when:
            underTest.updatePassword(id, userPasswordRequest)
        then:
            userPasswordRequest.getPassword() >> normalTextPassword
            1 * userRepository.findById(id) >> Optional.of(oldUser)
            1 * passwordEncoder.encode(normalTextPassword) >> encodedPassword
            1 * userRepository.save(oldUser)
        and:
            oldUser.password == encodedPassword
    }

    def "it should throw UserNotFoundException when updating password but user not found"() {
        given:
            String id = "123"

            UpdatePasswordRequest userPasswordRequest = Mock()
        when:
            underTest.updatePassword(id, userPasswordRequest)
        then:
            1 * userRepository.findById(id) >> Optional.empty()
            0 * passwordEncoder.encode(_)
            0 * userRepository.save(_)
        and:
            def ex = thrown(UserNotFoundException)
            ex.message == "User [ ${id} ] not found"
    }

    def "it should reset password for a given username"() {
        given:
            String userName = "123"
        when:
            underTest.resetPassword(userName)
        then:
            1 * userRepository.findByUsername(userName) >> Optional.of(userBO)
            1 * userPasswordTokenService.createToken(userBO)
    }

    def "it should reset password for a user"() {
        given:
            String id = "id"
            String email = "email"
            String token = "token"
            String normalTextPassword = "password"
            String encodedPassword = '24$36fQ$6'

            UserBO oldUser = new UserBO(id: id)

            ForgottenPasswordRequest forgottenPasswordRequest = Mock()
        when:
            underTest.resetPassword(forgottenPasswordRequest)
        then:
            forgottenPasswordRequest.getEmail() >> email
            forgottenPasswordRequest.getPassword() >> normalTextPassword
            forgottenPasswordRequest.getToken() >> token
            1 * userRepository.findByUsername(email) >> Optional.of(oldUser)
            1 * passwordEncoder.encode(normalTextPassword) >> encodedPassword
            1 * userPasswordTokenService.verifyToken(id, token)
            1 * userRepository.save(oldUser)
        and:
            oldUser.getPassword() == encodedPassword
    }

    def "it should verify email for a user"() {
        given:
            String id = "123"
        when:
            underTest.verifyEmail(id)
        then:
            1 * userRepository.findById(id) >> Optional.of(userBO)
            1 * emailVerificationTokenService.createToken(userBO)
    }

    def "it should verify email for a user for a given emailVerificationRequest"() {
        given:
            String userId = "userId"
            String token = "token"

            UserBO oldUser = new UserBO()
            EmailVerificationTokenBO emailVerificationTokenBO = Mock()
            EmailVerificationRequest emailVerificationRequest = Mock()
            UserResponse userResponse = Mock()
        when:
            underTest.verifyEmail(emailVerificationRequest)
        then:
            emailVerificationRequest.getToken() >> token
            emailVerificationTokenBO.getUserId() >> userId
            1 * emailVerificationTokenService.verifyToken(token) >> emailVerificationTokenBO
            1 * userRepository.findById(userId) >> Optional.of(oldUser)
            1 * userRepository.save(oldUser)
            1 * userResponseConverter.convert(oldUser) >>  userResponse
            1 * publishService.sendMessage(UserPublishAction.EMAIL_VERIFIED, userResponse)
        and:
            oldUser.emailVerified == true
    }
}
