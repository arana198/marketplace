package com.marketplace.user.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.dto.SocialUserRequest;
import org.springframework.stereotype.Service;

@Service
class SocialUserRequestConverter implements BaseConverter<SocialUserRequest, UserBO> {

     @Override
     public UserBO convert(final SocialUserRequest source) {
          return new UserBO()
              .setUsername(source.getEmail())
              .setEmailVerified(true);
     }
}
