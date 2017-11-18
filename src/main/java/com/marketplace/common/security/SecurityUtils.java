package com.marketplace.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SecurityUtils {

    public boolean checkIfUserAuthorized(final String userId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final List<String> roles = authentication.getAuthorities()
                .parallelStream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (roles.contains(UserRole.ROLE_ADMIN)) {
            return true;
        }

        if (!this.getUserInfo().containsValue(userId)) {
            throw new UnauthorizedUserException("Unauthorized user");
        }

        return true;
    }

    private static Map<String, String> getUserInfo() {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        return auth.getOAuth2Request().getRequestParameters();
    }
}
