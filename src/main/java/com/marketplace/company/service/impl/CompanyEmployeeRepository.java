package com.marketplace.company.service.impl;

import com.marketplace.company.domain.CompanyEmployeeBO;
import com.marketplace.common.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface CompanyEmployeeRepository extends BaseRepository<CompanyEmployeeBO, String> {
    Page<CompanyEmployeeBO> findByCompanyId(String companyId, Pageable pageable);

    List<CompanyEmployeeBO> findByCompanyIdAndAdminPrivilege(String companyId, boolean adminPrivilege);
}
