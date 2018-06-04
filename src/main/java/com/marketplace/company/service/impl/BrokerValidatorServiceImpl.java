package com.marketplace.company.service.impl;

import com.marketplace.company.domain.BrokerValidatorBO;
import com.marketplace.company.exception.BrokerNotFoundException;
import com.marketplace.company.queue.publish.CompanyPublishService;
import com.marketplace.company.queue.publish.domain.CompanyPublishAction;
import com.marketplace.company.service.BrokerService;
import com.marketplace.company.service.BrokerValidatorService;
import com.marketplace.company.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class BrokerValidatorServiceImpl implements BrokerValidatorService {

  private final BrokerValidatorRepository brokerValidatorRepository;
  private final BrokerService brokerService;
  private final CompanyService companyService;
  private final CompanyPublishService publishService;

  @Autowired
  BrokerValidatorServiceImpl(final BrokerValidatorRepository brokerValidatorRepository,
                             @Lazy final BrokerService brokerService,
                             final CompanyService companyService,
                             final CompanyPublishService publishService) {
    this.brokerValidatorRepository = brokerValidatorRepository;
    this.brokerService = brokerService;
    this.companyService = companyService;
    this.publishService = publishService;
  }

  @Override
  public void certificationVerified(final String companyId, final String brokerProfileId) {
    LOGGER.info("Verifying certificate for broker [ {} ]", brokerProfileId);
    try {
      BrokerValidatorBO brokerValidatorBO = this.getOrCreateBrokerValidatorBO(companyId, brokerProfileId)
          .setCertificateVerified(true);

      brokerValidatorRepository.save(brokerValidatorBO);
      if (companyService.findById(companyId).filter(company -> company.isActive()).isPresent()) {
        publishService.sendMessage(CompanyPublishAction.BROKER_VERIFIED, brokerService.findByCompanyIdAndBrokerProfileId(companyId, brokerProfileId).orElse(null));
      }
    } catch (BrokerNotFoundException e) {
      LOGGER.warn("Broker {} not found", brokerProfileId);
    }
  }

  @Override
  public boolean checkIfBrokerVerified(final String companyId, final String brokerProfileId) {
    if (brokerValidatorRepository.findByBrokerProfileId(brokerProfileId).map(BrokerValidatorBO::isCertificateVerified).orElse(false)
        && companyService.findById(companyId).filter(company -> company.isActive()).isPresent()) {

      return true;
    }

    return false;
  }

  private BrokerValidatorBO getOrCreateBrokerValidatorBO(final String companyId, final String brokerProfileId) throws BrokerNotFoundException {
    brokerService.findByCompanyIdAndBrokerProfileId(companyId, brokerProfileId)
        .orElseThrow(() -> new BrokerNotFoundException(companyId, brokerProfileId));

    return brokerValidatorRepository.findByBrokerProfileId(brokerProfileId)
        .orElse(this.createBrokerValidatorBO(brokerProfileId));
  }

  private BrokerValidatorBO createBrokerValidatorBO(final String brokerProfileId) {
    return new BrokerValidatorBO()
        .setBrokerProfileId(brokerProfileId);
  }
}
