package com.marketplace.user.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class UserRequest {

  @Size(min = 5, max = 50, message = "email is wrong size")
  @NotBlank(message = "email is mandatory")
  @Email(message = "email format incorrect")
  private final String email;

  @Size(min = 6, max = 30, message = "password is wrong size")
  @NotBlank(message = "password is mandatory")
  private final String password;

  private LoginProvider loginProvider = LoginProvider.LOCAL;

  @JsonCreator
  public UserRequest(@JsonProperty(value = "email") final String email,
                     @JsonProperty(value = "password") final String password) {
    this.email = email;
    this.password = password;
  }

  public enum LoginProvider {
    LOCAL("local"),
    FACEBOOK("facebook"),
    GOOGLE("google"),
    LINKEDIN("linkedin");

    private String value;

    LoginProvider(final String value) {
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

  public enum UserType {
    BROKER, COMPANY_ADMIN;
  }
}
