package com.marketplace.company.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Builder
@Data
@JsonDeserialize(builder = CompanyRegistrationRequest.CompanyRegistrationRequestBuilder.class)
@JsonIgnoreProperties
public class CompanyRegistrationRequest {

  @Valid
  @NotNull(message = "company is mandatory")
  private final CompanyRequest company;

  @Valid
  @NotNull(message = "brokerProfile is mandatory")
  private final BrokerProfileRequest brokerProfile;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CompanyRegistrationRequestBuilder {
  }
}
