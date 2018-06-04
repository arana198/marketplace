package com.marketplace.company.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.company.domain.BrokerDocumentBO;
import com.marketplace.company.dto.BrokerDocumentResponse;
import org.springframework.stereotype.Service;

@Service
class BrokerDocumentResponseConverter implements BaseConverter<BrokerDocumentBO, BrokerDocumentResponse> {

  @Override
  public BrokerDocumentResponse convert(final BrokerDocumentBO source) {
    return new BrokerDocumentResponse(
        source.getId(),
        source.getBrokerProfileId(),
        source.getFileId(),
        source.isVerified());
  }
}
