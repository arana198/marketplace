package com.marketplace.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marketplace.common.dto.BaseDomain;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Role extends BaseDomain {

    private final String roleId;

    @NotNull(message = "username is compulsory")
    private final UserRole name;

    @Size(min = 5, max = 50, message = "description is wrong size")
    @NotBlank(message = "description is compulsory")
    private final String description;

    @JsonCreator
    public Role(@JsonProperty(value = "roleId") final String roleId,
                @JsonProperty(value = "name") final UserRole name,
                @JsonProperty(value = "description") final String description) {
        this.roleId = roleId;
        this.name = name;
        this.description = description;
    }

    public enum UserRole {
        ROLE_ADMIN("ROLE_ADMIN"),
        ROLE_BROKER("ROLE_BROKER"),
        ROLE_USER("ROLE_USER");

        private String value;

        UserRole(String value) {
            this.value = value;
        }

        public static UserRole getRoleFromString(String value) {
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
}
