package com.marketplace.company.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.company.domain.CompanyBO;
import com.marketplace.company.dto.CompanyResponse;
import org.springframework.stereotype.Service;

@Service
class CompanyResponseConverter implements BaseConverter<CompanyBO, CompanyResponse> {

    @Override
    public CompanyResponse convert(final CompanyBO source) {
        return new CompanyResponse(source.getId(),
                source.getName(),
                source.getCompanyNumber(),
                source.getVatNumber(),
                source.getLogoUrl(),
                source.getWebsiteUrl(),
                source.isActive());
    }
}
