package com.marketplace.broker.profile.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class CompanyResponse extends ResourceSupport {
    private final String companyId;
    private final String name;
    private final String companyNumber;
    private final String vatNumber;
    private final String logoUrl;
    private final String websiteUrl;
}
