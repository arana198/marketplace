package com.marketplace.user.exception;

import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.user.dto.RoleRequest;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.RoleResponse;

public class RoleNotFoundException extends ResourceNotFoundException {
    public RoleNotFoundException() {
        super("No roles found");
    }

    public RoleNotFoundException(final String roleId) {
        super("RoleResponse [ " + roleId + " ] not found");
    }

    public RoleNotFoundException(final UserRole role) {
        super("UserRole [ " + role + " ] not found");
    }
}
