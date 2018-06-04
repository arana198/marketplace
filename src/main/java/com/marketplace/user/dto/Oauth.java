package com.marketplace.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Oauth {
  @NotBlank(message = "grantType is mandatory")
  private final String grantType;
  private final String username;
  private final String password;
  private final String providerId;
  private final String redirectUrl;
  private final String code;
  @JsonProperty("refresh_token")
  private final String refreshToken;

  @JsonCreator
  public Oauth(@JsonProperty(value = "grantType") final String grantType,
               @JsonProperty(value = "username") final String username,
               @JsonProperty(value = "password") final String password,
               @JsonProperty(value = "providerId") final String providerId,
               @JsonProperty(value = "redirectUrl") final String redirectUrl,
               @JsonProperty(value = "code") final String code,
               @JsonProperty(value = "refresh_token") final String refreshToken) {
    this.grantType = grantType;
    this.username = username;
    this.password = password;
    this.providerId = providerId;
    this.redirectUrl = redirectUrl;
    this.code = code;
    this.refreshToken = refreshToken;
  }
}
