package com.marketplace.company.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.company.domain.BrokerDocumentBO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface BrokerDocumentRepository extends BaseRepository<BrokerDocumentBO, String> {

    List<BrokerDocumentBO> findByBrokerProfileId(String brokerProfileId);
}
