package com.marketplace.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonDeserialize(builder = UserStatusRequest.UserStatusRequestBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserStatusRequest {

    public enum UserStatus {
        ACTIVE("ROLE_ADMIN"),
        PENDING("ROLE_ADMIN"),
        SUSPENDED("ROLE_ADMIN"),
        BLACKLISTED("ROLE_ADMIN"),
        CLOSED("ROLE_ADMIN"),
        PENDING_CLOSED("ROLE_ADMIN");

        private String value;

        UserStatus(String value) {
            this.value = value;
        }

        public static UserStatus getRoleFromString(String value) {
            for (UserStatus role : UserStatus.values()) {
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
    public static class UserStatusRequestBuilder {
    }
}
