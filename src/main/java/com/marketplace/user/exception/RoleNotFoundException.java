package com.marketplace.user.exception;

import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.user.dto.Role;

public class RoleNotFoundException extends ResourceNotFoundException {
    public RoleNotFoundException() {
        super("No roles found");
    }

    public RoleNotFoundException(final String roleId) {
        super("Role [ " + roleId + " ] not found");
    }

    public RoleNotFoundException(final Role.UserRole role) {
        super("UserRole [ " + role + " ] not found");
    }
}
