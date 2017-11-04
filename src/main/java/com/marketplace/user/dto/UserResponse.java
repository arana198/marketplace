package com.marketplace.user.dto;


import com.marketplace.common.dto.BaseDomain;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.AccessType;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserResponse extends BaseDomain {
    private final String userId;
    private final String email;
    private final List<UserRoleResponse> userRoles;
    private String profileImageUrl;
}
