package com.marketplace.storage.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

@Data
public class BucketResponse extends BaseResponseDomain {
  private final String bucketId;
  private final String type;
}
