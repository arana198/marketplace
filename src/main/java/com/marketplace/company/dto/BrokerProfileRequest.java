package com.marketplace.company.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@JsonDeserialize(builder = BrokerProfileRequest.BrokerProfileRequestBuilder.class)
public class BrokerProfileRequest {

  @NotBlank(message = "userId is mandatory")
  private final String userId;

  @NotBlank(message = "companyId is mandatory")
  private final String companyId;

  @NotBlank(message = "firstName is mandatory")
  private final String firstName;

  @NotBlank(message = "lastName is mandatory")
  private final String lastName;

  @NotBlank(message = "mobileNumber is mandatory")
  private final String mobileNumber;
  private final boolean active;
  private String aboutMe;

  @JsonPOJOBuilder(withPrefix = "")
  public static class BrokerProfileRequestBuilder {
  }
}
