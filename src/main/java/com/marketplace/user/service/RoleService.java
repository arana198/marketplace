package com.marketplace.user.service;

import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.dto.Role;
import com.marketplace.user.dto.Role.UserRole;
import com.marketplace.user.dto.RoleList;
import com.marketplace.user.exception.RoleNotFoundException;

import java.util.Optional;

public interface RoleService {
    Optional<RoleList> findActiveRoles();

    void updateRole(String roleId, Role role) throws RoleNotFoundException;

    Optional<RoleBO> findById(String id) throws RoleNotFoundException;

    Optional<RoleBO> findByName(UserRole role) throws RoleNotFoundException;
}



