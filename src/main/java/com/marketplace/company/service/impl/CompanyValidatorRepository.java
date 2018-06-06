package com.marketplace.company.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.company.domain.CompanyValidatorBO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CompanyValidatorRepository extends BaseRepository<CompanyValidatorBO, String> {

     Optional<CompanyValidatorBO> findByCompanyId(String companyId);
}
