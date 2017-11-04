package com.marketplace.user.controller;

import com.marketplace.common.exception.ResourceNotFoundException;
import com.marketplace.common.security.UserRole;
import com.marketplace.user.dto.Role;
import com.marketplace.user.dto.RoleList;
import com.marketplace.user.service.RoleService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Data
@Slf4j
@Controller
@ExposesResourceFor(Role.class)
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    @RolesAllowed({UserRole.ROLE_ADMIN})
    @GetMapping
    public ResponseEntity<RoleList> findActiveRoles() throws ResourceNotFoundException {
        return roleService.findActiveRoles()
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity(HttpStatus.OK));
    }

    @RolesAllowed({UserRole.ROLE_ADMIN})
    @PutMapping(value = "/{roleId}")
    public ResponseEntity<ResourceSupport> updateRole(@PathVariable final String roleId,
                                                      @RequestBody @Valid final Role role) throws ResourceNotFoundException {
        log.info("Updating role {}", roleId);
        roleService.updateRole(roleId, role);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
