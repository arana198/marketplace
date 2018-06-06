package com.marketplace.user.service.impl;

import com.marketplace.common.exception.InternalServerException;
import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.domain.UserRoleBO;
import com.marketplace.user.domain.UserStatusBO;
import com.marketplace.user.dto.EmailVerificationRequest;
import com.marketplace.user.dto.ForgottenPasswordRequest;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.SocialUserRequest;
import com.marketplace.user.dto.UpdatePasswordRequest;
import com.marketplace.user.dto.UserRequest;
import com.marketplace.user.dto.UserRequest.LoginProvider;
import com.marketplace.user.dto.UserRequest.UserType;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.dto.UserStatusRequest.UserStatus;
import com.marketplace.user.exception.EmailVerificationTokenNotFoundException;
import com.marketplace.user.exception.UserAlreadyExistsException;
import com.marketplace.user.exception.UserNotFoundException;
import com.marketplace.user.exception.UserPasswordTokenNotFoundException;
import com.marketplace.user.oauth.TokenRevoker;
import com.marketplace.user.queue.publish.UserPublishService;
import com.marketplace.user.queue.publish.domain.UserPublishAction;
import com.marketplace.user.service.RoleService;
import com.marketplace.user.service.UserService;
import com.marketplace.user.service.UserStatusService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
class UserServiceImpl implements UserService {

     private final UserRepository userRepository;
     private final RoleService roleService;
     private final UserStatusService userStatusService;
     private final UserPasswordTokenService userPasswordTokenService;
     private final EmailVerificationTokenService emailVerificationTokenService;
     private final TokenRevoker tokenRevoker;
     private final UserResponseConverter userResponseConverter;
     private final UserRequestConverter userRequestConverter;
     private final SocialUserRequestConverter socialUserRequestConverter;
     private final UserPublishService publishService;
     private final PasswordEncoder passwordEncoder;

     @Override
     public Optional<UserResponse> findByUsername(final String username) {
          return userRepository.findByUsername(username)
              .map(userResponseConverter::convert);
     }

     @Override
     public Optional<UserResponse> findById(final String userId) {
          return userRepository.findById(userId)
              .map(userResponseConverter::convert);
     }

     @Override
     public void addAsCompanyAdmin(final String userId) {
          this.updateUserRole(userId, UserRole.ROLE_BROKER, UserRole.ROLE_COMPANY_ADMIN);
     }

     @Override
     public void removeAsCompanyAdmin(final String userId) {
          this.updateUserRole(userId, UserRole.ROLE_COMPANY_ADMIN, UserRole.ROLE_BROKER);
     }

     @Override
     public void logout(final Principal principal) {
          LOGGER.debug("Logout user {}", principal.getName());
          tokenRevoker.revoke(principal.getName());
     }

     @Transactional
     @Override
     public void createUser(final UserRequest userRequest, final UserType userType) throws UserAlreadyExistsException {

          LOGGER.debug("Creating user {}", userRequest.getEmail());
          final UserRole role = userType == UserType.COMPANY_ADMIN ? UserRole.ROLE_COMPANY_ADMIN : UserRole.ROLE_BROKER;
          final Optional<UserBO> oldUser = userRepository.findByUsername(userRequest.getEmail());
          if (this.doesUserExists(oldUser, UserRole.ROLE_COMPANY_ADMIN) || this.doesUserExists(oldUser, UserRole.ROLE_BROKER)) {
               throw new UserAlreadyExistsException(userRequest.getEmail());
          }

          final UserBO userBO = oldUser.orElse(userRequestConverter.convert(userRequest));
          this.addUserRoleBO(userBO, role, LoginProvider.LOCAL.getValue().toString(), null, UserStatus.PENDING.getValue());
          final UserResponse newUser = userResponseConverter.convert(userBO);

          UserPublishAction publishAction = userType == UserType.COMPANY_ADMIN ? UserPublishAction.COMPANY_ADMIN_USER_CREATED : UserPublishAction.BROKER_USER_CREATED;
          publishService.sendMessage(publishAction, newUser);
          emailVerificationTokenService.createToken(userBO);
     }

     @Transactional
     @Override
     public void createUser(final SocialUserRequest socialUserRequest) throws UserAlreadyExistsException {
          LOGGER.debug("Creating social user {}", socialUserRequest.getEmail());
          final UserRole role = UserRole.ROLE_USER;
          final Optional<UserBO> oldUser = userRepository.findByUsername(socialUserRequest.getEmail());
          if (this.doesUserExists(oldUser, role)
              || userRepository.findByProviderUserId(socialUserRequest.getLoginProviderId()).isPresent()) {
               throw new UserAlreadyExistsException(socialUserRequest.getEmail());
          }

          final UserBO userBO = oldUser.orElse(socialUserRequestConverter.convert(socialUserRequest));
          this.addUserRoleBO(userBO, role, socialUserRequest.getLoginProvider().getValue().toString(), socialUserRequest.getLoginProviderId(), UserStatus.PENDING.getValue());
          final UserResponse newUser = userResponseConverter.convert(userBO)
              .setProfileImageUrl(socialUserRequest.getProfileImageUrl());

          publishService.sendMessage(UserPublishAction.USER_CREATED, newUser);
     }

     @Override
     public void resetPassword(final String username) {
          userRepository.findByUsername(username)
              .ifPresent(userPasswordTokenService::createToken);
     }

     @Transactional
     @Override
     public void resetPassword(final ForgottenPasswordRequest forgottenPasswordRequest)
         throws UserPasswordTokenNotFoundException, UserNotFoundException {

          final String email = forgottenPasswordRequest.getEmail();
          LOGGER.info("Resetting password for user: {}", email);
          final UserBO user = userRepository.findByUsername(email)
              .orElseThrow(() -> new UserNotFoundException(email));

          user.setPassword(passwordEncoder.encode(forgottenPasswordRequest.getPassword()));
          userPasswordTokenService.verifyToken(user.getId(), forgottenPasswordRequest.getToken());
          userRepository.save(user);
     }

     @Override
     public void updatePassword(final String userId, final UpdatePasswordRequest updatePasswordRequest) throws UserNotFoundException {
          LOGGER.info("Update password for user {}", userId);
          final UserBO userBO = userRepository.findById(userId)
              .orElseThrow(() -> new UserNotFoundException(userId));

          userBO.setPassword(passwordEncoder.encode(updatePasswordRequest.getPassword()));
          userRepository.save(userBO);
     }

     @Override
     public void verifyEmail(final String userId) {
          LOGGER.info("Create verification email for user {}", userId);
          userRepository.findById(userId)
              .ifPresent(emailVerificationTokenService::createToken);
     }

     @Override
     public void verifyEmail(final EmailVerificationRequest emailVerificationRequest) throws EmailVerificationTokenNotFoundException {
          LOGGER.info("Check verification email for token {}", emailVerificationRequest.getToken());
          final String userId = emailVerificationTokenService.verifyToken(emailVerificationRequest.getToken()).getUserId();
          userRepository.findById(userId)
              .map(user -> user.setEmailVerified(true))
              .ifPresent(user -> {
                   userRepository.save(user);
                   publishService.sendMessage(UserPublishAction.EMAIL_VERIFIED, userResponseConverter.convert(user));
              });
     }

     @Transactional
     @Override
     public void updateUserStatus(final String userId, final UserRole userRole, final UserStatus userStatus) throws UserNotFoundException {
          LOGGER.debug("Update user status for user {}", userId);
          final UserBO userBO = userRepository.findById(userId)
              .orElseThrow(() -> new UserNotFoundException(userId));

          userBO.getRoles()
              .parallelStream()
              .filter(ur -> ur.getRole().getName().equalsIgnoreCase(userRole.getValue()))
              .findAny()
              .ifPresent(oldUserRole -> {
                   final String oldUserStatus = oldUserRole.getUserStatus().getName();
                   final String newUserStatus;

                   if (UserStatus.ACTIVE.getValue().equalsIgnoreCase(oldUserStatus) && userStatus == UserStatus.CLOSED) {
                        newUserStatus = UserStatus.CLOSED.getValue();
                   } else if (UserStatus.PENDING.getValue().equalsIgnoreCase(oldUserStatus) && userStatus == UserStatus.CLOSED) {
                        newUserStatus = UserStatus.PENDING_CLOSED.getValue();
                   } else if (UserStatus.PENDING_CLOSED.getValue().equalsIgnoreCase(oldUserStatus) && userStatus == UserStatus.ACTIVE) {
                        newUserStatus = UserStatus.PENDING.getValue();
                   } else if (UserStatus.CLOSED.getValue().equalsIgnoreCase(oldUserStatus) && userStatus == UserStatus.ACTIVE) {
                        newUserStatus = UserStatus.ACTIVE.getValue();
                   } else if (UserStatus.PENDING.getValue().equalsIgnoreCase(oldUserStatus) && userStatus == UserStatus.ACTIVE) {
                        newUserStatus = UserStatus.ACTIVE.getValue();
                   } else {
                        throw new InternalServerException("Incorrect user status");
                   }

                   final UserStatusBO userStatusBO = userStatusService.findByName(userStatus.getValue()).get();
                   oldUserRole.setUserStatus(userStatusBO);

                   userRepository.save(userBO);
                   tokenRevoker.revoke(userId);
                   publishService.sendMessage(UserPublishAction.USER_STATUS_UPDATED, userResponseConverter.convert(userBO));
              });
     }

     private void updateUserRole(final String userId, final UserRole roleToChange, final UserRole roleChangedTo) {
          userRepository.findById(userId)
              .ifPresent(user -> {
                   user.getRoles()
                       .parallelStream()
                       .filter(ur -> ur.getRole().getName().equalsIgnoreCase(roleToChange.getValue()))
                       .findFirst()
                       .ifPresent(ur -> {
                            final RoleBO newRole = roleService.findByName(roleChangedTo).get();
                            ur.setRole(newRole);
                            userRepository.save(user);
                       });
              });
     }

     private boolean doesUserExists(final Optional<UserBO> oldUser, final UserRole role) {
          return oldUser.filter(u -> u.getRoles()
              .parallelStream()
              .map(UserRoleBO::getRole)
              .anyMatch(r -> r.getName().equalsIgnoreCase(role.getValue())))
              .isPresent();
     }

     private void addUserRoleBO(final UserBO userBO, final UserRole role, final String loginProvider, final String loginProviderUserId, final String userStatus) {
          final UserStatusBO userStatusBO = userStatusService.findByName(userStatus).get();
          final RoleBO userRole = roleService.findByName(role).get();

          final UserRoleBO userRoleBO = new UserRoleBO();
          userRoleBO.setUser(userBO);
          userRoleBO.setRole(userRole);
          userRoleBO.setUserStatus(userStatusBO);
          userRoleBO.setProvider(loginProvider);
          userRoleBO.setProviderUserId(loginProviderUserId);
          userBO.getRoles().add(userRoleBO);
          userRepository.save(userBO);
     }
}
