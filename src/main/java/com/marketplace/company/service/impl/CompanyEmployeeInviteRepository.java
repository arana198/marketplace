package com.marketplace.company.service.impl;

import com.marketplace.company.domain.CompanyEmployeeInviteBO;
import com.marketplace.common.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CompanyEmployeeInviteRepository extends BaseRepository<CompanyEmployeeInviteBO, String> {
    Optional<CompanyEmployeeInviteBO> findByEmail(String email);
    Optional<CompanyEmployeeInviteBO> findByCompanyIdAndToken(String companyId, String token);
}
