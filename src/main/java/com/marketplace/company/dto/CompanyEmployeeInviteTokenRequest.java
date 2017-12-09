package com.marketplace.company.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Builder
@Data
@JsonDeserialize(builder = CompanyEmployeeInviteTokenRequest.CompanyEmployeeInviteTokenRequestBuilder.class)
public class CompanyEmployeeInviteTokenRequest {
    @NotBlank(message = "token is mandatory")
    private final String token;

    @Valid
    @NotNull(message = "brokerProfileRequest is mandatory")
    private final BrokerProfileRequest brokerProfile;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CompanyEmployeeInviteTokenRequestBuilder {
    }
}
