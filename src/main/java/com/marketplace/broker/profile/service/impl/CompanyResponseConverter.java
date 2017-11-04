package com.marketplace.broker.profile.service.impl;

import com.marketplace.broker.profile.domain.CompanyBO;
import com.marketplace.broker.profile.dto.CompanyResponse;
import com.marketplace.common.converter.BaseConverter;
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
                source.getWebsiteUrl());
    }
}
