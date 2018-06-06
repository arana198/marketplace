package com.marketplace.company.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.company.domain.CompanyEmployeeInviteBO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CompanyEmployeeInviteRepository extends BaseRepository<CompanyEmployeeInviteBO, Long> {
     Optional<CompanyEmployeeInviteBO> findByCompanyIdAndEmail(String companyId, String email);

     Optional<CompanyEmployeeInviteBO> findByCompanyIdAndToken(String companyId, String token);
}
