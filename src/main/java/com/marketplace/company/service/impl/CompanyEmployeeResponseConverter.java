package com.marketplace.company.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.company.domain.CompanyEmployeeBO;
import com.marketplace.company.dto.CompanyEmployeeResponse;
import org.springframework.stereotype.Service;

@Service
class CompanyEmployeeResponseConverter implements BaseConverter<CompanyEmployeeBO, CompanyEmployeeResponse> {

    @Override
    public CompanyEmployeeResponse convert(final CompanyEmployeeBO source) {
        return new CompanyEmployeeResponse(source.getId(),
                source.getCompanyId(),
                source.getUserId());
    }
}
