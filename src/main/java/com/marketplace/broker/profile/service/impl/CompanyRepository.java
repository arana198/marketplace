package com.marketplace.broker.profile.service.impl;

import com.marketplace.broker.profile.domain.CompanyBO;
import com.marketplace.common.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CompanyRepository extends BaseRepository<CompanyBO, String> {

    Optional<CompanyBO> findByName(String name);

    List<CompanyBO> findByCompanyNumberOrVatNumber(String companyNumber, String vatNumber);

    Page<CompanyBO> findAll(Pageable pageable);
}
