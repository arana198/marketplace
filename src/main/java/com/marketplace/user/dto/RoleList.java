package com.marketplace.user.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleList extends BaseResponseDomain {
    private final List<RoleResponse> roleResponses;

    @JsonCreator
    public RoleList(@JsonProperty(value = "roleResponses") final List<RoleResponse> roleResponses) {
        this.roleResponses = roleResponses;
    }
}
