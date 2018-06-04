package com.marketplace.company.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

@Data
public class AdviceResponse extends BaseResponseDomain {
  private final String adviceId;
  private final String name;
  private final String description;
}
