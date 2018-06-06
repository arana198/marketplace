package com.marketplace.company.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

@Data
public class ServiceResponse extends BaseResponseDomain {
     private final String serviceId;
     private final String name;
     private final String description;
     private final String parentId;
}
