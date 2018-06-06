package com.marketplace.company.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyResponse extends BaseResponseDomain {
     private final String companyId;
     private final String name;
     private final String companyNumber;
     private final String vatNumber;
     private final String logoUrl;
     private final String websiteUrl;
     private final LocalDateTime fcaNumberVerifiedAt;
     private final boolean active;
}
