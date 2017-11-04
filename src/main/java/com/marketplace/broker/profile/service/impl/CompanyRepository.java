package com.marketplace.broker.profile.service.impl;

import com.marketplace.broker.profile.domain.CompanyBO;
import com.marketplace.common.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface CompanyRepository extends BaseRepository<CompanyBO, String> {

    List<CompanyBO> findByCompanyNumberOrVatNumber(String companyNumber, String vatNumber);
}
