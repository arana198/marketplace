package com.marketplace.user.service;

import com.marketplace.user.domain.UserStatusBO;
import com.marketplace.user.domain.UserStatusBO.UserStatus;
import com.marketplace.user.dto.Role.UserRole;
import com.marketplace.user.dto.User;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.exception.*;

import java.security.Principal;
import java.util.Optional;

public interface UserService {
    Optional<UserResponse> findByUsername(String username);

    void logout(Principal principal) throws UsernameNotFoundException;

    void createUser(User user, UserRole role) throws UserAlreadyExistsException, RoleNotFoundException;

    void updatePassword(String userId, String password) throws UserNotFoundException;

    void resetPassword(String userId, String token, String password) throws UserPasswordNotFoundTokenException, UserPasswordTokenExpiredException;

    void updateUserStatus(String userId, UserRole userRole, UserStatus userStatus) throws UserNotFoundException;
}
