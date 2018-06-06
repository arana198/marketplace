package com.marketplace.user.service;

import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.dto.RoleList;
import com.marketplace.user.dto.RoleRequest;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.exception.RoleNotFoundException;

import java.util.Optional;

public interface RoleService {
     Optional<RoleList> findActiveRoles();

     void updateRole(String roleId, RoleRequest roleRequest) throws RoleNotFoundException;

     Optional<RoleBO> findById(String id);

     Optional<RoleBO> findByName(UserRole role);
}



