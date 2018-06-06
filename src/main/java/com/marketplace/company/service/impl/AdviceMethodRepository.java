package com.marketplace.company.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.company.domain.AdviceMethodBO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface AdviceMethodRepository extends BaseRepository<AdviceMethodBO, String> {

     Optional<AdviceMethodBO> findByName(String name);

     List<AdviceMethodBO> findByActive(boolean active);
}
