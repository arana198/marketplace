package com.marketplace.broker.profile.service.impl;

import com.marketplace.broker.profile.domain.CompanyBO;
import com.marketplace.broker.profile.dto.CompanyRequest;
import com.marketplace.common.converter.BaseConverter;
import org.springframework.stereotype.Service;

@Service
class CompanyRequestConverter implements BaseConverter<CompanyRequest, CompanyBO> {

    @Override
    public CompanyBO convert(final CompanyRequest source) {
        return new CompanyBO()
                .setName(source.getName())
                .setCompanyNumber(source.getCompanyNumber())
                .setVatNumber(source.getVatNumber())
                .setLogoUrl(source.getLogoUrl())
                .setWebsiteUrl(source.getWebsiteUrl())
                .setActive(source.isActive());
    }
}
