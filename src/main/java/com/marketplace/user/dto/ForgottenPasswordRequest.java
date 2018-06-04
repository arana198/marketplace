package com.marketplace.user.dto;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
@Accessors(chain = true)
@JsonDeserialize(builder = ForgottenPasswordRequest.ForgottenPasswordRequestBuilder.class)
public class ForgottenPasswordRequest {

  @NotBlank(message = "email is mandatory")
  @Email(message = "email format incorrect")
  private final String email;

  @Size(min = 6, max = 30, message = "password is wrong size")
  @NotBlank(message = "password is mandatory")
  private final String password;

  @NotBlank(message = "token is mandatory")
  private final String token;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ForgottenPasswordRequestBuilder {
  }
}
