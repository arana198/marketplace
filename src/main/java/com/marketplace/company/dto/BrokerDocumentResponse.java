package com.marketplace.company.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

@Data
public class BrokerDocumentResponse extends BaseResponseDomain {
    private final String brokerDocumentId;
    private final String brokerProfileId;
    private final String fileId;
    private final boolean verified;
}
