package com.marketplace.company.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.company.domain.BrokerValidatorBO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface BrokerValidatorRepository extends BaseRepository<BrokerValidatorBO, String> {

     Optional<BrokerValidatorBO> findByBrokerProfileId(String brokerProfileId);
}
