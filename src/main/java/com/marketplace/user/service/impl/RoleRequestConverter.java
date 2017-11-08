package com.marketplace.user.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.common.converter.Converter;
import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.dto.RoleList;
import com.marketplace.user.dto.RoleRequest;
import com.marketplace.user.dto.RoleResponse;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

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
