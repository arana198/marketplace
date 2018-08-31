package com.marketplace.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

import java.util.List;

@Data
public class RoleList extends BaseResponseDomain {
     private final List<RoleResponse> roles;

     @JsonCreator
     public RoleList(@JsonProperty(value = "roles") final List<RoleResponse> roles) {
          this.roles = roles;
     }
}
