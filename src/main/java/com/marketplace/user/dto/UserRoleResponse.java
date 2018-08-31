package com.marketplace.user.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

@Data
public class UserRoleResponse extends BaseResponseDomain {
     private final String role;
     private final String userStatus;
}
