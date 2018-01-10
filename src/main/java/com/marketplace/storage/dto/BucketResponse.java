package com.marketplace.storage.dto;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BucketResponse extends BaseResponseDomain {
    private final String bucketId;
    private final String type;
}
