package com.marketplace.storage.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.storage.domain.BucketBO;
import com.marketplace.storage.dto.BucketRequest;
import org.springframework.stereotype.Service;

@Service
class BucketRequestConverter implements BaseConverter<BucketRequest, BucketBO> {

     @Override
     public BucketBO convert(final BucketRequest source) {
          return (BucketBO) new BucketBO()
              .setType(source.getBucketType().getValue())
              .setId(source.getBucketId());
     }
}
