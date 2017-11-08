package com.marketplace.user.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.user.domain.RoleBO;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

interface RoleRepository extends BaseRepository<RoleBO, String> {

    @Cacheable("RoleRepository.findById")
    Optional<RoleBO> findById(String id);

    @Cacheable("RoleRepository.findByFulltextName")
    Optional<RoleBO> findByName(String role);

    @Cacheable("RoleRepository.findBySelectable")
    List<RoleBO> findBySelectable(Boolean selectable);
}
