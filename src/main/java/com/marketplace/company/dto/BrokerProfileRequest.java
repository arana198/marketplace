package com.marketplace.company.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonDeserialize(builder = BrokerProfileRequest.BrokerProfileRequestBuilder.class)
public class BrokerProfileRequest {

    private final String userId;

    private final String companyId;

    private final String firstName;

    private final String lastName;

    private final String mobileNumber;

    private final String aboutMe;

    private final String imageUrl;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BrokerProfileRequestBuilder {
    }
}
