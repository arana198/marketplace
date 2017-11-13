package com.marketplace.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SecurityUtils {

    public void checkIfUserAuthorized(final String userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities()
                .parallelStream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equalsIgnoreCase(UserRole.ROLE_ADMIN)) && !this.getUserInfo().containsValue(userId)) {
            throw new UnauthorizedUserException("Unauthorized user");
        }
    }

    private static Map<String, String> getUserInfo() {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        return auth.getOAuth2Request().getRequestParameters();
    }

    //TODO: Decrypt enhanced token
    //TODO: IsActive annotation per role
    //TODO: Restrict user to its own resource
}
