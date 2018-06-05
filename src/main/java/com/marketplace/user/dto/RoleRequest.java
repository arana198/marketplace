package com.marketplace.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@Data
@JsonDeserialize(builder = RoleRequest.RoleRequestBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoleRequest {

  private final String roleId;

  @NotNull(message = "name is mandatory")
  private final UserRole name;

  @Size(min = 5, max = 50, message = "description is wrong size")
  @NotBlank(message = "description is mandatory")
  private final String description;

  @NotNull(message = "isSelectable is mandatory")
  private final boolean isSelectable;

  public enum UserRole {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_COMPANY_ADMIN("ROLE_COMPANY_ADMIN"),
    ROLE_BROKER("ROLE_BROKER"),
    ROLE_USER("ROLE_USER");

    private final String value;

    UserRole(final String value) {
      this.value = value;
    }

    public static UserRole getRoleFromString(final String value) {
      for (UserRole role : UserRole.values()) {
        if (role.getValue().equalsIgnoreCase(value)) {
          return role;
        }
      }

      return null;
    }

    public String getValue() {
      return this.value;
    }
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class RoleRequestBuilder {
  }
}
