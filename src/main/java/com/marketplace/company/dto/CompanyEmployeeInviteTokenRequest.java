package com.marketplace.company.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Builder
@Data
@JsonDeserialize(builder = CompanyEmployeeInviteTokenRequest.CompanyEmployeeInviteTokenRequestBuilder.class)
public class CompanyEmployeeInviteTokenRequest {
    @NotBlank(message = "token is mandatory")
    private final String token;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CompanyEmployeeInviteTokenRequestBuilder {
    }
}
