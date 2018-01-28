package com.marketplace.company.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@Builder
@JsonDeserialize(builder = ServiceRequest.ServiceRequestRequestBuilder.class)
public class ServiceRequest {

    @NotBlank(message = "name is mandatory")
    private final String name;

    @NotBlank(message = "description is mandatory")
    private final String description;

    private final String parentServiceId;

    @NotNull(message = "active is mandatory")
    private final boolean active;

    @JsonPOJOBuilder(withPrefix = "")
    public static class ServiceRequestRequestBuilder {
    }
}
