package com.marketplace.user.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class EmailVerificationRequest {

  @NotBlank(message = "token is mandatory")
  private final String token;

  @JsonCreator
  public EmailVerificationRequest(@JsonProperty(value = "token") final String token) {
    this.token = token;
  }
}
