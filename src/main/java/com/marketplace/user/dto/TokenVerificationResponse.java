package com.marketplace.user.dto;


import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TokenVerificationResponse extends BaseResponseDomain {
  private final String userId;
  private final String email;
  private final String token;
  private final LocalDateTime createdTs;
}
