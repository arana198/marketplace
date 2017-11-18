package com.marketplace.company.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Builder
@Data
@JsonDeserialize(builder = CompanyEmployeeRequest.CompanyEmployeeRequestBuilder.class)
public class CompanyEmployeeRequest {
    @NotBlank(message = "isAdmin is mandatory")
    private final boolean isAdmin;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CompanyEmployeeRequestBuilder {
    }
}
