package com.marketplace.company.service;

public interface BrokerValidatorService {

     void certificationVerified(String companyId, String brokerProfileId);

     boolean checkIfBrokerVerified(String companyId, String brokerProfileId);
}
