package com.marketplace.company.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

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
