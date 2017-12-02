package com.marketplace.company.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.company.domain.BrokerProfileBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface BrokerProfileRepository extends BaseRepository<BrokerProfileBO, String> {

    List<BrokerProfileBO> findByUserId(String userId);

    Page<BrokerProfileBO> findByCompanyId(String companyId, Pageable pageable);

    List<BrokerProfileBO> findByCompanyIdAndAdmin(String companyId, boolean admin);
}
