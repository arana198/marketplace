package com.marketplace.user.oauth;

import com.marketplace.user.dto.Role.UserRole;
import com.marketplace.user.dto.User;
import com.marketplace.user.dto.User.LoginProvider;
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
                    User user = new User(profile.getEmail(), null);
                    LoginProvider loginProvider = LoginProvider.getRoleFromString(connection.getKey().getProviderId());
                    user.setLoginProvider(loginProvider);
                    user.setLoginProviderId(connection.getKey().getProviderUserId());
                    user.setProfileImageUrl(connection.getImageUrl());

                    try {
                        userService.createUser(user, UserRole.ROLE_USER);
                        return user.getEmail();
                    } catch (UserAlreadyExistsException | RoleNotFoundException e) {
                        return null;
                    }
                });
    }
}
