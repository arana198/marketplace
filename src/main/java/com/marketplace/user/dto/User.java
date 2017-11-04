package com.marketplace.user.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class User {

    public enum LoginProvider {
        LOCAL("local"),
        FACEBOOK("facebook"),
        GOOGLE("google"),
        LINKEDIN("linkedin");

        private String value;

        LoginProvider(String value) {
            this.value = value;
        }

        public static LoginProvider getRoleFromString(final String value) {
            for (LoginProvider loginProvider : LoginProvider.values()) {
                if (loginProvider.getValue().equalsIgnoreCase(value)) {
                    return loginProvider;
                }
            }

            return null;
        }

        public String getValue() {
            return this.value;
        }
    }

    @Size(min = 5, max = 50, message = "email is wrong size")
    @NotBlank(message = "email is compulsory")
    @Email(message = "email format incorrect")
    private final String email;

    @Size(min = 6, max = 30, message = "password is wrong size")
    @NotBlank(message = "password is compulsory")
    private final String password;

    @JsonIgnore
    private String userId;

    @JsonIgnore
    private RoleList roles;

    @JsonIgnore
    private LoginProvider loginProvider = LoginProvider.LOCAL;

    @JsonIgnore
    private String loginProviderId;

    @JsonIgnore
    private String profileImageUrl;

    @JsonIgnore
    private String userStatus;

    @JsonCreator
    public User(@JsonProperty(value = "email") final String email,
                @JsonProperty(value = "password") final String password) {
        this.email = email;
        this.password = password;
    }
}
