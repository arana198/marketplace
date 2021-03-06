package com.marketplace.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Builder
@Data
@JsonDeserialize(builder = UserProfileRequest.UserProfileBuilder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileRequest {

     @Size(min = 1, max = 50, message = "firstName is wrong size")
     @NotBlank(message = "firstName is mandatory")
     @Pattern(regexp = "[A-Za-z ]*", message = "firstName has invalid characters")
     private final String firstName;

     @Size(min = 1, max = 50, message = "lastName is wrong size")
     @NotBlank(message = "lastName is mandatory")
     @Pattern(regexp = "[A-Za-z ]*", message = "lastName has invalid characters")
     private final String lastName;

     @Size(min = 1, max = 11, message = "mobileNumber is wrong size")
     @NotBlank(message = "mobileNumber is mandatory")
     @Pattern(regexp = "^(07\\d{9})$", message = "mobileNumber is invalid")
     private final String mobileNumber;

     @NotNull(message = "location is mandatory")
     private final String postcode;

     @NotBlank(message = "userId is mandatory")
     private String userId;
     private LocalDate dateOfBirth;

     @JsonIgnore
     private String email;

     @JsonPOJOBuilder(withPrefix = "")
     public static class UserProfileBuilder {
     }
}
