package com.marketplace.common.annotation.handler;

import com.marketplace.common.annotation.IsActive;
import com.marketplace.common.exception.ResourceForbiddenException;
import com.marketplace.common.security.AuthUser;
import com.marketplace.user.dto.UserStatusRequest.UserStatus;
import com.marketplace.utils.RolesExtractorUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class IsActiveInterceptorHandler extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
    final HandlerMethod handlerMethod = (HandlerMethod) handler;
    final IsActive isActive = handlerMethod.getMethodAnnotation(IsActive.class);

    if (isActive != null) {
      final Map<String, String> stringMap = RolesExtractorUtils.extract(AuthUser.getRoles());
      for (int i = 0; i < isActive.roles().length; i++) {
        String status = stringMap.get(isActive.roles()[i]);
        if (status != null && !status.equalsIgnoreCase(UserStatus.ACTIVE.getValue())) {
          throw new ResourceForbiddenException("User forbidden");
        }
      }
    }

    return true;
  }
}
