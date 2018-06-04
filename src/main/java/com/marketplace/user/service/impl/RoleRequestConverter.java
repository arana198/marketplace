package com.marketplace.user.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.dto.RoleRequest;
import org.springframework.stereotype.Service;

@Service
public class RoleRequestConverter implements BaseConverter<RoleRequest, RoleBO> {
  @Override
  public RoleBO convert(final RoleRequest source) {
    return new RoleBO()
        .setName(source.getName().getValue())
        .setDescription(source.getDescription())
        .setSelectable(source.isSelectable());
  }
}
