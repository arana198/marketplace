package com.marketplace.company.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.company.domain.ServiceBO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface ServiceRepository extends BaseRepository<ServiceBO, String> {

     List<ServiceBO> findByParentAndActive(ServiceBO serviceBO, boolean active);

     Optional<ServiceBO> findByName(String name);
}
