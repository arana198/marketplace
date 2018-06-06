package com.marketplace.user.oauth;

import com.marketplace.user.dto.UserResponse;
import com.marketplace.user.dto.UserRoleResponse;
import com.marketplace.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
class CustomTokenEnhancer implements TokenEnhancer {

     private final UserService userService;
     private final List<TokenEnhancer> delegates = Collections.emptyList();

     @Override
     public OAuth2AccessToken enhance(final OAuth2AccessToken accessToken, final OAuth2Authentication authentication) {
          DefaultOAuth2AccessToken tempResult = (DefaultOAuth2AccessToken) accessToken;

          final Map<String, Object> additionalInformation = new HashMap<>();
          final String username = authentication.getName().toString();
          final UserResponse user = userService.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException(authentication.getName()));

          final Map<String, String> roles = user.getUserRoles()
              .parallelStream()
              .collect(Collectors.toMap(UserRoleResponse::getRole, UserRoleResponse::getUserStatus));

          additionalInformation.put("userId", user.getUserId());
          additionalInformation.put("username", username);
          additionalInformation.put("roles", roles);
          tempResult.setAdditionalInformation(additionalInformation);

          OAuth2AccessToken result = tempResult;
          for (TokenEnhancer enhancer : delegates) {
               result = enhancer.enhance(result, authentication);
          }
          return result;
     }
}

