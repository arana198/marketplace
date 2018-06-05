package com.marketplace.company.exception;

import com.marketplace.common.exception.ResourceNotFoundException;

public class BrokerNotFoundException extends ResourceNotFoundException {
  public BrokerNotFoundException(final String companyId, final String brokerProfileId) {
    super("Broker [ " + brokerProfileId + " ] for company [ " + companyId + " ] not found or expired");
  }
}
