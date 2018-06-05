package com.marketplace.user.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.dto.UserRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
class UserRequestConverter implements BaseConverter<UserRequest, UserBO> {

  private final PasswordEncoder passwordEncoder;

  @Override
  public UserBO convert(final UserRequest source) {
    return new UserBO()
        .setUsername(source.getEmail())
        .setPassword(passwordEncoder.encode(source.getPassword()));
  }
}
