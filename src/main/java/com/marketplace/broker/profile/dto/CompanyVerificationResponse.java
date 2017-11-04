package com.marketplace.broker.profile.dto;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
public class CompanyVerificationResponse {
    @JsonProperty("company_name")
    private final String name;
}