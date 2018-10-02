package com.marketplace.company.service.impl;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.company.domain.BrokerProfileBO;
import com.marketplace.company.domain.CompanyBO;
import com.marketplace.company.dto.CompanyRegistrationRequest;
import com.marketplace.company.dto.CompanyRequest;
import com.marketplace.company.dto.CompanyResponse;
import com.marketplace.company.exception.BrokerAlreadyRegisteredException;
import com.marketplace.company.exception.CompanyAlreadyExistsException;
import com.marketplace.company.exception.CompanyNotFoundException;
import com.marketplace.company.queue.publish.CompanyPublishService;
import com.marketplace.company.queue.publish.domain.CompanyPublishAction;
import com.marketplace.company.service.CompanyService;
import com.marketplace.company.service.CompanyValidatorService;
import com.marketplace.company.validator.CompanyNumberValidator;
import com.marketplace.company.validator.VATValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
class CompanyServiceImpl implements CompanyService {

     private final CompanyRepository companyRepository;
     private final BrokerProfileRepository brokerProfileRepository;
     private final CompanyValidatorService companyValidatorService;
     private final CompanyResponseConverter companyResponseConverter;
     private final CompanyRequestConverter companyRequestConverter;
     private final BrokerProfileRequestConverter brokerProfileRequestConverter;
     private final VATValidator vatValidator;
     private final CompanyNumberValidator companyValidator;
     private final CompanyPublishService publishService;

     @Override
     public Page<CompanyResponse> findAll(final String companyName, final Pageable pageable) {
          return Optional.ofNullable(companyName)
              .map(cn -> this.companyRepository.findByFulltextName(cn, pageable))
              .orElse(this.companyRepository.findAll(pageable))
              .map(this.companyResponseConverter::convert);
     }

     @Override
     public Optional<CompanyResponse> findById(final String companyId) {
          return companyRepository.findById(companyId)
              .map(companyResponseConverter::convert);
     }

     @Transactional
     @Override
     public CompanyResponse createCompany(final String userId, final CompanyRegistrationRequest companyRegistrationRequest)
         throws CompanyNotFoundException, CompanyAlreadyExistsException, BrokerAlreadyRegisteredException {

          final CompanyRequest company = companyRegistrationRequest.getCompany();
          if (!companyRepository.findByCompanyNumberOrVatNumber(company.getCompanyNumber(), company.getVatNumber()).isEmpty()) {
               LOGGER.info("Company Number [ {} ] or VAT Number [ {} ] already exists", company.getCompanyNumber(), company.getVatNumber());
               throw new CompanyAlreadyExistsException(company.getCompanyNumber(), company.getVatNumber());
          }

          if (!companyRegistrationRequest.getBrokerProfile().getUserId().equalsIgnoreCase(userId)) {
               throw new BadRequestException("Invalid user id");
          }

          if (!brokerProfileRepository.findByUserId(userId).isEmpty()) {
               throw new BrokerAlreadyRegisteredException(userId);
          }

          if (!vatValidator.validate(company.getName(), company.getVatNumber())) {
               throw new BadRequestException("Invalid VAT number");
          }

          if (!companyValidator.validate(company.getName(), company.getCompanyNumber())) {
               throw new BadRequestException("Invalid company number");
          }

          //TODO: FCA Number API integration
          CompanyBO companyBO = companyRequestConverter.convert(company)
              .setActive(false);

          companyBO = companyRepository.save(companyBO);

          final CompanyResponse companyResponse = companyResponseConverter.convert(companyBO);

          final BrokerProfileBO brokerProfileBO = brokerProfileRequestConverter.convert(companyRegistrationRequest.getBrokerProfile());
          brokerProfileBO.setCompanyId(companyBO.getId());
          brokerProfileBO.setAdmin(true);

          brokerProfileRepository.save(brokerProfileBO);
          publishService.sendMessage(CompanyPublishAction.COMPANY_CREATED, companyResponse);

          return companyResponse;
     }

     @Override
     public void updateCompany(final String companyId, final CompanyRequest companyRequest)
         throws CompanyNotFoundException, CompanyAlreadyExistsException {

          final CompanyBO oldCompany = companyRepository.findById(companyId)
              .orElseThrow(() -> new CompanyNotFoundException(companyId));

          if (companyRepository.findByName(companyRequest.getName())
              .filter(companyBO -> !companyBO.getId().equalsIgnoreCase(companyId))
              .isPresent()) {
               throw new CompanyAlreadyExistsException(companyRequest.getName());
          }

          if (!oldCompany.getVatNumber().equalsIgnoreCase(companyRequest.getVatNumber())
              && !vatValidator.validate(companyRequest.getName(), companyRequest.getVatNumber())) {

               throw new BadRequestException("Invalid VAT number");
          }

          if (!oldCompany.getCompanyNumber().equalsIgnoreCase(companyRequest.getCompanyNumber())
              && !companyValidator.validate(companyRequest.getName(), companyRequest.getCompanyNumber())) {

               throw new BadRequestException("Invalid company number");
          }

          CompanyBO newCompanyBO = companyRequestConverter.convert(companyRequest);

          if (!oldCompany.getFcaNumber().equalsIgnoreCase(companyRequest.getFcaNumber())) {
               companyValidatorService.fcaNumberUnverified(companyId);
          } else if (!companyValidatorService.checkIfCompanyVerified(companyId) && newCompanyBO.isActive()) {
               newCompanyBO.setActive(false);
          }

          newCompanyBO.update(oldCompany);
          newCompanyBO = companyRepository.save(newCompanyBO);
          final CompanyResponse companyResponse = companyResponseConverter.convert(newCompanyBO);

          if (oldCompany.isActive() && !newCompanyBO.isActive()) {
               publishService.sendMessage(CompanyPublishAction.COMPANY_INACTIVATED, companyResponse);
          } else if (!oldCompany.isActive() && newCompanyBO.isActive()) {
               publishService.sendMessage(CompanyPublishAction.COMPANY_ACTIVATED, companyResponse);
          }

          publishService.sendMessage(CompanyPublishAction.COMPANY_UPDATED, companyResponse);
     }

     @Override
     public void activateOrInactiveCompany(final String companyId, final boolean activate) throws CompanyNotFoundException {
          final CompanyBO companyBO = companyRepository.findById(companyId)
              .map(company -> company.setActive(activate))
              .orElseThrow(() -> new CompanyNotFoundException(companyId));

          companyRepository.save(companyBO);

          final CompanyResponse companyResponse = companyResponseConverter.convert(companyBO);

          if (activate) {
               publishService.sendMessage(CompanyPublishAction.COMPANY_ACTIVATED, companyResponse);
          } else {
               publishService.sendMessage(CompanyPublishAction.COMPANY_INACTIVATED, companyResponse);
          }
     }
}