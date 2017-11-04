package com.marketplace.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.marketplace.common.dto.BaseDomain;
import com.vividsolutions.jts.geom.Polygon;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
@JsonDeserialize(builder = UserProfileResponse.UserProfileResponseBuilder.class)
@JsonIgnoreProperties
public class UserProfileResponse extends BaseDomain {

    private final String profileId;
    private final String userId;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final String mobileNumber;
    private final LocalDate dateOfBirth;
    private final String postcode;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserProfileResponseBuilder {
    }
}
