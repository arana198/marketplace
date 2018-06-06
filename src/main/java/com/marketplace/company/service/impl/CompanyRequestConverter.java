package com.marketplace.company.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.company.domain.CompanyBO;
import com.marketplace.company.dto.CompanyRequest;
import org.springframework.stereotype.Service;

@Service
class CompanyRequestConverter implements BaseConverter<CompanyRequest, CompanyBO> {

     @Override
     public CompanyBO convert(final CompanyRequest source) {
          return new CompanyBO()
              .setName(source.getName())
              .setCompanyNumber(source.getCompanyNumber())
              .setVatNumber(source.getVatNumber())
              .setFcaNumber(source.getFcaNumber())
              .setLogoUrl(source.getLogoUrl())
              .setWebsiteUrl(source.getWebsiteUrl())
              .setActive(source.isActive());
     }
}
