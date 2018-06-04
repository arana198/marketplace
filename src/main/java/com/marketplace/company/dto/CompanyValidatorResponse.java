package com.marketplace.company.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyValidatorResponse extends BaseResponseDomain {
  private final String companyId;
  private final boolean fcaNumberVerified;
  private final boolean billingActive;
  private final LocalDateTime fcaNumberVerifiedAt;
}
