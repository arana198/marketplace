package com.marketplace.storage.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.storage.domain.BucketBO;
import com.marketplace.storage.dto.BucketResponse;
import org.springframework.stereotype.Service;

@Service
class BucketReponseConverter implements BaseConverter<BucketBO, BucketResponse> {
  @Override
  public BucketResponse convert(final BucketBO source) {
    return new BucketResponse(
        source.getId(),
        source.getType());
  }
}
