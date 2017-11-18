package com.marketplace.user.dto;


import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserResponse extends BaseResponseDomain {
    private final String userId;
    private final String email;
    private final List<UserRoleResponse> userRoles;
    private final boolean emailVerified;
    private String profileImageUrl;
}
