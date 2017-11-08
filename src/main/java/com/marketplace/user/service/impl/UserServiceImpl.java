package com.marketplace.user.service.impl;

import com.marketplace.queue.publish.PublishService;
import com.marketplace.queue.publish.domain.PublishAction;
import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.domain.UserPasswordTokenBO;
import com.marketplace.user.domain.UserRoleBO;
import com.marketplace.user.domain.UserStatusBO;
import com.marketplace.user.domain.UserStatusBO.UserStatus;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.UserRequest;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.exception.RoleNotFoundException;
import com.marketplace.user.exception.UserAlreadyExistsException;
import com.marketplace.user.exception.UserNotFoundException;
import com.marketplace.user.exception.UserPasswordNotFoundTokenException;
import com.marketplace.user.exception.UserPasswordTokenExpiredException;
import com.marketplace.user.exception.UsernameNotFoundException;
import com.marketplace.user.oauth.TokenRevoker;
import com.marketplace.user.service.RoleService;
import com.marketplace.user.service.UserPasswordTokenService;
import com.marketplace.user.service.UserService;
import com.marketplace.user.service.UserStatusService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Slf4j
@Service
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserStatusService userStatusService;
    private final UserPasswordTokenService userPasswordTokenService;
    private final TokenRevoker tokenRevoker;
    private final UserResponseConverter userResponseConverter;
    private final UserConverter userConverter;
    private final PublishService publishService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserResponse> findByUsername(final String username) {
        return userRepository.findByUsername(username)
                .map(userResponseConverter::convert);
    }

    @Override
    public void logout(final Principal principal) throws UsernameNotFoundException {
        log.debug("Logout pending {}", principal.getName());
        userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));

        tokenRevoker.revoke(principal.getName());
    }

    @Override
    @Transactional
    public void createUser(final UserRequest userRequest, final UserRole role) throws UserAlreadyExistsException, RoleNotFoundException {

        log.debug("Creating pending {}", userRequest.getEmail());
        final Optional<UserBO> oldUser = userRepository.findByUsername(userRequest.getEmail());
        if (oldUser.filter(u -> u.getRoles()
                .parallelStream()
                .map(UserRoleBO::getRole)
                .anyMatch(r -> r.getName().equalsIgnoreCase(role.getValue())))
                .isPresent()
                || userRepository.findByProviderUserId(userRequest.getLoginProviderId()).isPresent()) {
            throw new UserAlreadyExistsException(userRequest.getEmail());
        }

        final UserBO userBO = oldUser.orElse(userConverter.convert(userRequest));
        final UserStatusBO userStatusBO = userStatusService.findByName(UserStatus.PENDING).get();
        final RoleBO userRole = roleService.findByName(role)
                .orElseThrow(() -> new RoleNotFoundException(role));

        final UserRoleBO userRoleBO = new UserRoleBO();
        userRoleBO.setUser(userBO);
        userRoleBO.setRole(userRole);
        userRoleBO.setUserStatus(userStatusBO);
        userBO.getRoles().add(userRoleBO);

        userRepository.save(userBO);
        final UserResponse newUser = userResponseConverter.convert(userBO)
                .setProfileImageUrl(userRequest.getProfileImageUrl());

        publishService.sendMessage(PublishAction.USER_CREATED, newUser);
    }

    @Override
    @Transactional
    public void resetPassword(final String userId,
                              final String token,
                              final String password) throws UserPasswordNotFoundTokenException, UserPasswordTokenExpiredException {
        final UserPasswordTokenBO userPasswordToken = userPasswordTokenService.findByUserIdAndToken(userId, token)
                .orElseThrow(() -> new UserPasswordNotFoundTokenException(userId, token));

        if (userPasswordToken.getCreatedTs().compareTo(LocalDateTime.now().minusDays(2)) <= 0) {
            log.debug("Token generated at {} for pending id {} and token {} has expired", userPasswordToken.getCreatedTs(), userId, token);
            throw new UserPasswordTokenExpiredException(userId, token, userPasswordToken.getCreatedTs().plusDays(2));
        }

        log.info("Resetting password for pending {}", userId);
        UserBO user = userPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
        userPasswordTokenService.delete(userPasswordToken);
    }

    @Override
    public void updatePassword(final String userId, final String password) throws UserNotFoundException {
        log.info("Update password for pending {}", userId);
        final UserBO userBO = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        log.debug("Updating password for pending {}", userId);
        userBO.setPassword(passwordEncoder.encode(password));
        userRepository.save(userBO);
    }

    @Override
    @Transactional
    public void updateUserStatus(final String userId, final UserRole userRole, final UserStatus userStatus) throws UserNotFoundException {
        log.debug("Update pending domain for pending {}", userId);
        final UserBO userBO = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        final UserStatusBO userStatusBO = userStatusService.findByName(userStatus).get();
        final Optional<UserRoleBO> userRoleBO = userBO.getRoles()
                .parallelStream()
                .filter(ur -> ur.getRole().getName().equalsIgnoreCase(userRole.getValue()))
                .findAny();

        userRoleBO.ifPresent(ur -> ur.setUserStatus(userStatusBO));
        userRepository.save(userBO);
        tokenRevoker.revoke(userId);
        publishService.sendMessage(PublishAction.USER_STATUS_UPDATED, userResponseConverter.convert(userBO));
    }
}
