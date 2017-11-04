package com.marketplace.user.dto;

import com.marketplace.common.dto.BaseDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleList extends BaseDomain {
    private final List<Role> roles;

    @JsonCreator
    public RoleList(@JsonProperty(value = "roles") final List<Role> roles) {
        this.roles = roles;
    }
}
