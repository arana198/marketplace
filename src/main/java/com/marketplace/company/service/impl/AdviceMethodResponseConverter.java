package com.marketplace.company.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.company.domain.AdviceMethodBO;
import com.marketplace.company.dto.AdviceResponse;
import org.springframework.stereotype.Service;

@Service
class AdviceMethodResponseConverter implements BaseConverter<AdviceMethodBO, AdviceResponse> {

    @Override
    public AdviceResponse convert(final AdviceMethodBO source) {
        return new AdviceResponse(
                source.getId(),
                source.getDisplayName(),
                source.getDescription()
        );
    }
}
