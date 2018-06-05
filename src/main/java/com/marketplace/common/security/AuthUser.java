package com.marketplace.common.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class AuthUser {

  public static final String USERNAME = "username";
  public static final String USER_ID = "userId";
  public static final String USER_STATUS = "userStatus";
  public static final String ROLES = "roles";

  private AuthUser() {

  }

  public static String getUserId() {
    return getUserInfo().get(USER_ID);
  }

  public static String getUserStatus() {
    return getUserInfo().get(USER_STATUS);
  }

  public static String getRoles() {
    return getUserInfo().get(ROLES);
  }

  public static Map<String, String> getUserInfo() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .filter(auth -> !auth.getPrincipal().equals("anonymousUser"))
        .map(OAuth2Authentication.class::cast)
        .map(OAuth2Authentication::getOAuth2Request)
        .map(OAuth2Request::getRequestParameters)
        .orElse(new HashMap<>());
  }
}
