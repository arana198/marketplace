package com.marketplace.user.service.impl;

import com.marketplace.common.converter.Converter;
import com.marketplace.user.dto.Role;
import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.dto.RoleList;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleConverter implements Converter<RoleBO, Role> {

    @Override
    public RoleBO convert(Role source) {
        return new RoleBO();
    }

    @Override
    public Role convert(final RoleBO source) {
        return new Role(source.getId(),
                Role.UserRole.getRoleFromString(source.getName()),
                source.getDescription());
    }

    public RoleList convert(final Set<RoleBO> source) {
        return new RoleList(
                source.stream()
                        .map(this::convert)
                        .collect(Collectors.toList())
        );
    }
}
