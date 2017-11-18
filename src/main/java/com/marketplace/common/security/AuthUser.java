package com.marketplace.common.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.util.Map;

public final class AuthUser {

    private AuthUser() {

    }

    public static String getUserId() {
        return getUserInfo().get("userId");
    }

    public static String getUserStatus() {
        return getUserInfo().get("userStatus");
    }

    public static String getRoles() {
        return getUserInfo().get("roles");
    }

    public static Map<String, String> getUserInfo() {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        return auth.getOAuth2Request().getRequestParameters();
    }
}
