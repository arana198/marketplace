package com.marketplace.company.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CompanyVerificationResponse {
    @JsonProperty("company_name")
    private final String name;
}