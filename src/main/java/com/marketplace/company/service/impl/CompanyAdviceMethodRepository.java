package com.marketplace.company.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.company.domain.CompanyAdviceMethodBO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CompanyAdviceMethodRepository extends BaseRepository<CompanyAdviceMethodBO, String> {

    List<CompanyAdviceMethodBO> findByCompanyId(String companyId);

    Optional<CompanyAdviceMethodBO> findByCompanyIdAndAdviceId(String companyId, String adviceId);
}
