package com.marketplace.user.service;

import com.marketplace.user.domain.UserStatusBO.UserStatus;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.SocialUserRequest;
import com.marketplace.user.dto.UserRequest;
import com.marketplace.user.dto.UserRequest.UserType;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.exception.RoleNotFoundException;
import com.marketplace.user.exception.UserAlreadyExistsException;
import com.marketplace.user.exception.UserNotFoundException;
import com.marketplace.user.exception.UserPasswordNotFoundTokenException;
import com.marketplace.user.exception.UserPasswordTokenExpiredException;
import com.marketplace.user.exception.UsernameNotFoundException;

import java.security.Principal;
import java.util.Optional;

public interface UserService {
    Optional<UserResponse> findByUsername(String username);

    void logout(Principal principal) throws UsernameNotFoundException;

    void createUser(UserRequest userRequest, UserType userType) throws UserAlreadyExistsException;

    void createUser(SocialUserRequest userRequest) throws UserAlreadyExistsException;

    void updatePassword(String userId, String password) throws UserNotFoundException;

    void resetPassword(String username) throws UserNotFoundException;

    void resetPassword(String userId, String token, String password) throws UserPasswordNotFoundTokenException, UserPasswordTokenExpiredException;

    void updateUserStatus(String userId, UserRole userRole, UserStatus userStatus) throws UserNotFoundException;
}
