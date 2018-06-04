package com.marketplace.company.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.company.domain.CompanyServiceBO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CompanyServiceRepository extends BaseRepository<CompanyServiceBO, String> {

  List<CompanyServiceBO> findByCompanyId(String companyId);

  Optional<CompanyServiceBO> findByCompanyIdAndServiceId(String companyId, String serviceId);
}
