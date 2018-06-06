package com.marketplace.user.oauth;

import com.marketplace.user.dto.SocialUserRequest;
import com.marketplace.user.dto.UserRequest.LoginProvider;
import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.exception.UserAlreadyExistsException;
import com.marketplace.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
class SocialConnectionSignUp implements ConnectionSignUp {
     private final UserService userService;

     @Override
     public String execute(final Connection<?> connection) {
          final UserProfile profile = connection.fetchUserProfile();
          return userService.findByUsername(profile.getEmail())
              .map(UserResponse::getEmail)
              .orElseGet(() -> {
                   LoginProvider loginProvider = LoginProvider.getRoleFromString(connection.getKey().getProviderId());
                   SocialUserRequest socialUserRequest = (SocialUserRequest) new SocialUserRequest(
                       profile.getEmail(),
                       connection.getKey().getProviderUserId(),
                       connection.getImageUrl())
                       .setLoginProvider(loginProvider);

                   try {
                        userService.createUser(socialUserRequest);
                        return socialUserRequest.getEmail();
                   } catch (UserAlreadyExistsException e) {
                        return null;
                   }
              });
     }
}
