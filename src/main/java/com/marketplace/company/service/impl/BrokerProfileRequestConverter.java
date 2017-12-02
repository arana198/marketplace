package com.marketplace.company.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.company.domain.BrokerProfileBO;
import com.marketplace.company.dto.BrokerProfileRequest;
import com.marketplace.company.dto.BrokerProfileResponse;
import org.springframework.stereotype.Service;

@Service
class BrokerProfileRequestConverter implements BaseConverter<BrokerProfileRequest, BrokerProfileBO> {

    @Override
    public BrokerProfileBO convert(final BrokerProfileRequest source) {
        return new BrokerProfileBO()
                .setCompanyId(source.getCompanyId())
                .setUserId(source.getUserId())
                .setFirstName(source.getFirstName())
                .setLastName(source.getLastName())
                .setMobileNumber(source.getMobileNumber())
                .setAboutMe(source.getAboutMe())
                .setImageUrl(source.getImageUrl())
                .setAdmin(false)
                .setActive(false);
    }
}
