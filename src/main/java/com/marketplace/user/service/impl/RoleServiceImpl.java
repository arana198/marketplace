package com.marketplace.user.service.impl;

import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.dto.Role;
import com.marketplace.user.dto.RoleList;
import com.marketplace.user.exception.RoleNotFoundException;
import com.marketplace.user.service.RoleService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Slf4j
@Service
class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleConverter roleConverter;

    public Optional<RoleList> findActiveRoles() {
        log.debug("Find all active pending roles");
        return Optional.ofNullable(new RoleList(roleRepository.findBySelectable(true)
                .stream()
                .map(r -> roleConverter.convert(r))
                .collect(Collectors.toList())));
    }

    @Transactional
    public void updateRole(final String roleId, final Role role) throws RoleNotFoundException {
        log.debug("Update role {}", roleId, role);
        roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));

        final RoleBO roleBO = roleConverter.convert(role)
                .setId(roleId);

        roleRepository.save(roleBO);
    }

    @Override
    public Optional<RoleBO> findById(final String id) throws RoleNotFoundException {
        return roleRepository.findById(id);
    }


    public Optional<RoleBO> findByName(final Role.UserRole role) throws RoleNotFoundException {
        return roleRepository.findByName(role.getValue());
    }
}



