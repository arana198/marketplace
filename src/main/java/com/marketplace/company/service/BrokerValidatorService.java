package com.marketplace.company.service;

import com.marketplace.company.exception.BrokerNotFoundException;

public interface BrokerValidatorService {

    void certificationVerified(String companyId, String brokerProfileId);

    boolean checkIfBrokerVerified(String companyId, String brokerProfileId);
}
