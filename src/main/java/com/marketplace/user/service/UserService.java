package com.marketplace.user.service;

import com.marketplace.user.dto.EmailVerificationRequest;
import com.marketplace.user.dto.ForgottenPasswordRequest;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.SocialUserRequest;
import com.marketplace.user.dto.UpdatePasswordRequest;
import com.marketplace.user.dto.UserRequest;
import com.marketplace.user.dto.UserRequest.UserType;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.dto.UserStatusRequest.UserStatus;
import com.marketplace.user.exception.EmailVerificationTokenNotFoundException;
import com.marketplace.user.exception.UserAlreadyExistsException;
import com.marketplace.user.exception.UserNotFoundException;
import com.marketplace.user.exception.UserPasswordTokenNotFoundException;

import java.security.Principal;
import java.util.Optional;

public interface UserService {
  Optional<UserResponse> findByUsername(String username);

  Optional<UserResponse> findById(String userId);

  void addAsCompanyAdmin(String userId);

  void removeAsCompanyAdmin(String userId);

  void logout(Principal principal);

  void createUser(UserRequest userRequest, UserType userType) throws UserAlreadyExistsException;

  void createUser(SocialUserRequest userRequest) throws UserAlreadyExistsException;

  void updatePassword(String userId, UpdatePasswordRequest updatePasswordRequest) throws UserNotFoundException;

  void verifyEmail(String userId);

  void verifyEmail(EmailVerificationRequest emailVerificationRequest) throws EmailVerificationTokenNotFoundException;

  void resetPassword(String username);

  void resetPassword(ForgottenPasswordRequest forgottenPasswordRequest) throws UserPasswordTokenNotFoundException, UserNotFoundException;

  void updateUserStatus(String userId, UserRole userRole, UserStatus userStatus) throws UserNotFoundException;
}
