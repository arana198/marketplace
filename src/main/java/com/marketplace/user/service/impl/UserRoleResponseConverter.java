package com.marketplace.user.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.user.domain.UserRoleBO;
import com.marketplace.user.dto.UserRoleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class UserRoleResponseConverter implements BaseConverter<UserRoleBO, UserRoleResponse> {

  private final RoleResponseConverter roleConverter;

  @Autowired
  public UserRoleResponseConverter(final RoleResponseConverter roleConverter) {
    this.roleConverter = roleConverter;
  }

  @Override
  public UserRoleResponse convert(final UserRoleBO source) {
    return new UserRoleResponse(source.getRole().getName(), source.getUserStatus().getName());
  }

  public List<UserRoleResponse> convert(final Set<UserRoleBO> source) {
    return source.parallelStream()
        .map(this::convert)
        .collect(Collectors.toList());
  }
}
