package com.marketplace.company.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.company.domain.CompanyBO;
import com.marketplace.company.dto.CompanyResponse;
import com.marketplace.company.dto.CompanyValidatorResponse;
import com.marketplace.company.service.CompanyValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
class CompanyResponseConverter implements BaseConverter<CompanyBO, CompanyResponse> {

  private final CompanyValidatorService companyValidatorService;

  @Override
  public CompanyResponse convert(final CompanyBO source) {
    LocalDateTime fcaNumberVerifiedAt = companyValidatorService.findByCompanyId(source.getId())
        .map(CompanyValidatorResponse::getFcaNumberVerifiedAt)
        .orElse(null);

    return new CompanyResponse(source.getId(),
        source.getName(),
        source.getCompanyNumber(),
        source.getVatNumber(),
        source.getLogoUrl(),
        source.getWebsiteUrl(),
        fcaNumberVerifiedAt,
        source.isActive());
  }
}
