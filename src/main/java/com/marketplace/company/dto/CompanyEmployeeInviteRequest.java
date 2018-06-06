package com.marketplace.company.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@JsonDeserialize(builder = CompanyEmployeeInviteRequest.CompanyEmployeeInviteRequestBuilder.class)
public class CompanyEmployeeInviteRequest {
     @NotBlank(message = "email is mandatory")
     private final String email;

     @JsonPOJOBuilder(withPrefix = "")
     public static class CompanyEmployeeInviteRequestBuilder {
     }
}
