package com.marketplace.company.service;

import com.marketplace.company.dto.CompanyValidatorResponse;
import com.marketplace.company.exception.CompanyNotFoundException;

import java.util.Optional;

public interface CompanyValidatorService {

  void fcaNumberUnverified(String companyId) throws CompanyNotFoundException;

  void fcaNumberVerified(String companyId);

  void billingIsActive(String companyId);

  void billingInactivated(String companyId) throws CompanyNotFoundException;

  boolean checkIfCompanyVerified(String companyId);

  Optional<CompanyValidatorResponse> findByCompanyId(String companyId);
}
