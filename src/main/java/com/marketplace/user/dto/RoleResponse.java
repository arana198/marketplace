package com.marketplace.user.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

@Data
public class RoleResponse extends BaseResponseDomain {
    private final String roleId;
    private final String name;
    private final String description;
    private final boolean isSelectable;
}
