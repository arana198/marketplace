package com.marketplace.company.service.impl;

import com.marketplace.company.domain.CompanyValidatorBO;
import com.marketplace.company.dto.CompanyValidatorResponse;
import com.marketplace.company.exception.CompanyNotFoundException;
import com.marketplace.company.queue.publish.CompanyPublishService;
import com.marketplace.company.queue.publish.domain.CompanyPublishAction;
import com.marketplace.company.service.CompanyService;
import com.marketplace.company.service.CompanyValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
class CompanyValidatorServiceImpl implements CompanyValidatorService {

     private final CompanyValidatorRepository companyValidatorRepository;
     private final CompanyService companyService;
     private final CompanyPublishService publishService;

     @Autowired
     CompanyValidatorServiceImpl(final CompanyValidatorRepository companyValidatorRepository,
                                 @Lazy final CompanyService companyService,
                                 final CompanyPublishService publishService) {
          this.companyValidatorRepository = companyValidatorRepository;
          this.companyService = companyService;
          this.publishService = publishService;
     }

     @Transactional
     @Override
     public void fcaNumberUnverified(final String companyId) throws CompanyNotFoundException {
          CompanyValidatorBO brokerValidatorBO = this.getOrCreateCompanyValidatorBO(companyId)
              .setFcaNumberVerified(false)
              .setFcaNumberVerifiedAt(null);

          companyValidatorRepository.save(brokerValidatorBO);
          companyService.activateOrInactiveCompany(companyId, false);
     }

     @Transactional
     @Override
     public void fcaNumberVerified(final String companyId) {
          LOGGER.info("Verifying FCA number for company [ {} ]", companyId);
          try {
               CompanyValidatorBO companyValidatorBO = this.getOrCreateCompanyValidatorBO(companyId)
                   .setFcaNumberVerified(true)
                   .setFcaNumberVerifiedAt(LocalDateTime.now());

               companyValidatorRepository.save(companyValidatorBO);
               this.activateCompany(companyValidatorBO);
               publishService.sendMessage(CompanyPublishAction.COMPANY_FCA_NUMBER_VERIFIED, companyService.findById(companyId).orElse(null));
          } catch (CompanyNotFoundException e) {
               LOGGER.warn("Company {} not found", companyId);
          }
     }

     @Transactional
     @Override
     public void billingIsActive(final String companyId) {
          LOGGER.info("Activating billing for company [ {} ]", companyId);
          try {
               CompanyValidatorBO companyValidatorBO = this.getOrCreateCompanyValidatorBO(companyId)
                   .setBillingActive(true);

               companyValidatorRepository.save(companyValidatorBO);
               this.activateCompany(companyValidatorBO);
          } catch (CompanyNotFoundException e) {
               LOGGER.warn("Company {} not found", companyId);
          }
     }

     @Override
     public void billingInactivated(final String companyId) throws CompanyNotFoundException {
          CompanyValidatorBO brokerValidatorBO = this.getOrCreateCompanyValidatorBO(companyId)
              .setBillingActive(false);

          companyValidatorRepository.save(brokerValidatorBO);
          companyService.activateOrInactiveCompany(companyId, false);
     }


     @Override
     public boolean checkIfCompanyVerified(final String companyId) {
          return companyValidatorRepository.findByCompanyId(companyId)
              .map(company -> company.isFcaNumberVerified())
              .orElse(false);
     }

     @Override
     public Optional<CompanyValidatorResponse> findByCompanyId(final String companyId) {
          return companyValidatorRepository.findByCompanyId(companyId)
              .map(cv -> new CompanyValidatorResponse(cv.getCompanyId(),
                  cv.isFcaNumberVerified(),
                  cv.isBillingActive(),
                  cv.getFcaNumberVerifiedAt()));
     }

     private CompanyValidatorBO getOrCreateCompanyValidatorBO(final String companyId) throws CompanyNotFoundException {
          companyService.findById(companyId)
              .orElseThrow(() -> new CompanyNotFoundException(companyId));

          return companyValidatorRepository.findByCompanyId(companyId)
              .orElse(this.createCompanyValidatorBO(companyId));
     }

     private CompanyValidatorBO createCompanyValidatorBO(final String companyId) {
          return new CompanyValidatorBO()
              .setCompanyId(companyId);
     }

     private void activateCompany(final CompanyValidatorBO companyValidatorBO) throws CompanyNotFoundException {
          if (companyValidatorBO.isFcaNumberVerified() && companyValidatorBO.isBillingActive()) {
               companyService.activateOrInactiveCompany(companyValidatorBO.getCompanyId(), true);
          }
     }

     //TODO: Automate FCA status check monthly
}
