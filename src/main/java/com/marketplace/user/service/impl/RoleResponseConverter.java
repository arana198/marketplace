package com.marketplace.user.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.dto.RoleList;
import com.marketplace.user.dto.RoleResponse;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleResponseConverter implements BaseConverter<RoleBO, RoleResponse> {

  @Override
  public RoleResponse convert(final RoleBO source) {
    return new RoleResponse(source.getId(),
        source.getName(),
        source.getDescription(),
        source.isSelectable());
  }

  public RoleList convert(final Set<RoleBO> source) {
    return new RoleList(
        source.stream()
            .map(this::convert)
            .collect(Collectors.toList())
    );
  }
}
