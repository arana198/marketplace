package com.marketplace.storage.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import com.marketplace.company.dto.BrokerProfileResponse;
import lombok.Data;

import java.util.List;

@Data
public class BucketPermissionResponse extends BaseResponseDomain {
     private final String bucketId;
     private final List<BrokerProfileResponse> brokerProfiles;
}
