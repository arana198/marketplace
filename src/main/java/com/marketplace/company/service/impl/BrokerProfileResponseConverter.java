package com.marketplace.company.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.company.domain.BrokerProfileBO;
import com.marketplace.company.dto.BrokerProfileResponse;
import org.springframework.stereotype.Service;

@Service
class BrokerProfileResponseConverter implements BaseConverter<BrokerProfileBO, BrokerProfileResponse> {

  @Override
  public BrokerProfileResponse convert(final BrokerProfileBO source) {
    return new BrokerProfileResponse(
        source.getId(),
        source.getUserId(),
        source.getCompanyId(),
        source.getFirstName(),
        source.getLastName(),
        source.getMobileNumber(),
        source.getAboutMe(),
        source.getImageUrl(),
        source.isAdmin(),
        source.isActive());
  }
}
