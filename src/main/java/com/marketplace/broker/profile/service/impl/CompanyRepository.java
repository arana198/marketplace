package com.marketplace.broker.profile.service.impl;

import com.marketplace.broker.profile.domain.CompanyBO;
import com.marketplace.common.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface CompanyRepository extends BaseRepository<CompanyBO, String> {

    @Query(value = "SELECT * FROM users WHERE MATCH (name) AGAINST (':name') ORDER BY name \\n#pageable\\n",
            countQuery = "SELECT count(*) FROM users WHERE MATCH (name) AGAINST (':name') ORDER BY name \\n#pageable\\n",
            nativeQuery = true)
    Page<CompanyBO> findByName(@Param("name") String name, Pageable pageable);

    List<CompanyBO> findByCompanyNumberOrVatNumber(String companyNumber, String vatNumber);

    Page<CompanyBO> findAll(Pageable pageable);
}
