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
          ACTIVE("ACTIVE"),
          PENDING("PENDING"),
          SUSPENDED("SUSPENDED"),
          BLACKLISTED("BLACKLISTED"),
          CLOSED("CLOSED"),
          PENDING_CLOSED("PENDING_CLOSED");

          private final String value;

          UserStatus(final String value) {
               this.value = value;
          }

          public static UserStatus getRoleFromString(final String value) {
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
