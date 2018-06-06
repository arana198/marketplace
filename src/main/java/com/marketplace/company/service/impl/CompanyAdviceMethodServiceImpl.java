package com.marketplace.company.service.impl;

import com.marketplace.company.domain.CompanyAdviceMethodBO;
import com.marketplace.company.dto.CompanyAdviceMethodResponse;
import com.marketplace.company.exception.AdviceMethodNotFoundException;
import com.marketplace.company.exception.CompanyAdviceAlreadyExistsException;
import com.marketplace.company.service.AdviceMethodService;
import com.marketplace.company.service.CompanyAdviceMethodService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
class CompanyAdviceMethodServiceImpl implements CompanyAdviceMethodService {

     private final CompanyAdviceMethodRepository companyAdviceMethodRepository;
     private final AdviceMethodService adviceMethodService;

     @Override
     public CompanyAdviceMethodResponse getCompanyAdvice(final String companyId) {
          LOGGER.debug("Getting advice method for company [ {} ]", companyId);
          List<String> adviceMethods = companyAdviceMethodRepository.findByCompanyId(companyId)
              .parallelStream()
              .map(CompanyAdviceMethodBO::getAdviceId)
              .collect(Collectors.toList());

          return new CompanyAdviceMethodResponse(adviceMethods);
     }

     @Override
     public void addCompanyAdvice(final String companyId, final String adviceId)
         throws CompanyAdviceAlreadyExistsException, AdviceMethodNotFoundException {

          LOGGER.debug("Adding advice method to company [ {} ]", companyId);
          if (companyAdviceMethodRepository.findByCompanyIdAndAdviceId(companyId, adviceId).isPresent()) {
               throw new CompanyAdviceAlreadyExistsException(adviceId);
          }

          adviceMethodService.getAdviceMethods()
              .parallelStream()
              .filter(advice -> advice.getAdviceId().equalsIgnoreCase(adviceId))
              .findAny()
              .orElseThrow(() -> new AdviceMethodNotFoundException(adviceId));

          CompanyAdviceMethodBO companyAdviceMethodBO = new CompanyAdviceMethodBO()
              .setCompanyId(companyId)
              .setAdviceId(adviceId);

          companyAdviceMethodRepository.save(companyAdviceMethodBO);
          //TODO: Publish company added service
     }

     @Override
     public void removeCompanyAdvice(final String companyId, final String adviceId) {
          LOGGER.debug("Removing advice method [ {} ] from the company [ {} ]", adviceId, companyId);
          companyAdviceMethodRepository.findByCompanyIdAndAdviceId(companyId, adviceId)
              .ifPresent(companyAdviceMethodRepository::delete);
     }
}
