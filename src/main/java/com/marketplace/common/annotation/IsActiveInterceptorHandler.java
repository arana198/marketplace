package com.marketplace.common.annotation;

import com.marketplace.common.exception.ResourceForbiddenException;
import com.marketplace.common.security.AuthUser;
import com.marketplace.user.domain.UserStatusBO.UserStatus;
import com.marketplace.utils.RolesExtractorUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class IsActiveInterceptorHandler extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final IsActive isActive = handlerMethod.getMethodAnnotation(IsActive.class);

        if (isActive != null) {
            final Map<String, String> stringMap = RolesExtractorUtils.extract(AuthUser.getRoles());
            for (int i = 0; i < isActive.roles().length; i++) {
                String role = stringMap.get(isActive.roles()[i]);
                if (role != null && !role.equalsIgnoreCase(UserStatus.ACTIVE.toString())) {
                    throw new ResourceForbiddenException("User forbidden");
                }
            }
        }

        return true;
    }
}
