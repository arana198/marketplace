package com.marketplace.user.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
@JsonDeserialize(builder = UpdatePasswordRequest.UpdatePasswordRequestBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePasswordRequest {

     @Size(min = 6, max = 30, message = "password is wrong size")
     @NotBlank(message = "password is mandatory")
     private final String password;

     @JsonPOJOBuilder(withPrefix = "")
     public static class UpdatePasswordRequestBuilder {
     }
}
