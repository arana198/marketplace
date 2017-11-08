package com.marketplace.user.service.impl;

import com.marketplace.user.domain.RoleBO;
import com.marketplace.user.dto.RoleRequest;
import com.marketplace.user.dto.RoleRequest.UserRole;
import com.marketplace.user.dto.RoleResponse;
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
    private final RoleResponseConverter roleResponseConverter;
    private final RoleRequestConverter roleRequestConverter;

    public Optional<RoleList> findActiveRoles() {
        log.debug("Find all active pending roles");
        return Optional.ofNullable(new RoleList(roleRepository.findBySelectable(true)
                .stream()
                .map(roleResponseConverter::convert)
                .collect(Collectors.toList())));
    }

    @Transactional
    public void updateRole(final String roleId, final RoleRequest roleRequest) throws RoleNotFoundException {
        log.debug("Update roleResponse {}", roleId, roleRequest);
        final RoleBO oldRoleBO = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException(roleId));

        //Not allowed to change name
        final RoleBO roleBO = roleRequestConverter.convert(roleRequest)
                .setId(roleId)
                .setName(oldRoleBO.getName());

        roleRepository.save(roleBO);
    }

    @Override
    public Optional<RoleBO> findById(final String id) throws RoleNotFoundException {
        return roleRepository.findById(id);
    }


    public Optional<RoleBO> findByName(final UserRole role) throws RoleNotFoundException {
        return roleRepository.findByName(role.getValue());
    }
}



