package com.marketplace.user.oauth;

import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.UserRequest;
import com.marketplace.user.dto.UserRequest.LoginProvider;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.exception.RoleNotFoundException;
import com.marketplace.user.exception.UserAlreadyExistsException;
import com.marketplace.user.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;

@Data
@Slf4j
@Component
public class SocialConnectionSignUp implements ConnectionSignUp {
    private final UserService userService;

    @Override
    public String execute(final Connection<?> connection) {
        final UserProfile profile = connection.fetchUserProfile();
        return userService.findByUsername(profile.getEmail())
                .map(UserResponse::getEmail)
                .orElseGet(() -> {
                    UserRequest userRequest = new UserRequest(profile.getEmail(), null);
                    LoginProvider loginProvider = LoginProvider.getRoleFromString(connection.getKey().getProviderId());
                    userRequest.setLoginProvider(loginProvider);
                    userRequest.setLoginProviderId(connection.getKey().getProviderUserId());
                    userRequest.setProfileImageUrl(connection.getImageUrl());

                    try {
                        userService.createUser(userRequest, UserRole.ROLE_USER);
                        return userRequest.getEmail();
                    } catch (UserAlreadyExistsException | RoleNotFoundException e) {
                        return null;
                    }
                });
    }
}
