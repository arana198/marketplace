package com.marketplace.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marketplace.common.dto.BaseDomain;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRoleResponse extends BaseDomain {
    private final String role;
    private final String userStatus;
}
