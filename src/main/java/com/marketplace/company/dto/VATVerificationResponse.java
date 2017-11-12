package com.marketplace.company.dto;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
public class VATVerificationResponse {
    private final boolean valid;
    private final Name name;

    @Data
    public class Name {
        @JsonProperty("0")
        private final String name;
    }
}