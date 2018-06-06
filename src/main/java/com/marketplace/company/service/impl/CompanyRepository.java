package com.marketplace.company.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.company.domain.CompanyBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface CompanyRepository extends BaseRepository<CompanyBO, String> {

     @Query(value = "SELECT * FROM users WHERE MATCH (name) AGAINST (':name') ORDER BY name \\n#pageable\\n",
         countQuery = "SELECT count(*) FROM users WHERE MATCH (name) AGAINST (':name') ORDER BY name \\n#pageable\\n",
         nativeQuery = true)
     Page<CompanyBO> findByFulltextName(@Param("name") String name, Pageable pageable);

     Optional<CompanyBO> findByName(String name);

     List<CompanyBO> findByCompanyNumberOrVatNumber(String companyNumber, String vatNumber);

     Page<CompanyBO> findAll(Pageable pageable);
}
