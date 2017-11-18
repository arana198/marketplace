package com.marketplace.company.dto;


import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class InviteBrokerTokenVerificationResponse extends BaseResponseDomain {
    private final String companyId;
    private final String companyName;
    private final String email;
    private final String token;
}
