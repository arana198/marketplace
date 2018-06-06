package com.marketplace.company.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
@Data
@JsonDeserialize(builder = CompanyRequest.CompanyRequestBuilder.class)
@JsonIgnoreProperties
public class CompanyRequest {

     @Size(min = 1, max = 60, message = "name is wrong size")
     @NotBlank(message = "name is mandatory")
     @Pattern(regexp = "[A-Za-z ]*", message = "name has invalid characters")
     private final String name;

     @Size(min = 8, max = 8, message = "companyNumber is wrong size")
     @NotBlank(message = "companyNumber is mandatory")
     @Pattern(regexp = "[0-9]*", message = "companyNumber has invalid characters")
     private final String companyNumber;

     @Size(min = 9, max = 9, message = "vatNumber is wrong size")
     @NotBlank(message = "vatNumber is mandatory")
     @Pattern(regexp = "[0-9]*", message = "vatNumber is invalid")
     private final String vatNumber;

     @Size(min = 9, max = 9, message = "fcaNumber is wrong size")
     @NotBlank(message = "fcaNumber is mandatory")
     @Pattern(regexp = "[0-9]*", message = "fcaNumber is invalid")
     private final String fcaNumber;

     @URL(message = "logoUrl is incorrect format")
     private final String logoUrl;

     @URL(message = "websiteUrl is incorrect format")
     private final String websiteUrl;

     @NotNull(message = "isActive is mandatory")
     private final boolean isActive;

     @JsonPOJOBuilder(withPrefix = "")
     public static class CompanyRequestBuilder {
     }
}
