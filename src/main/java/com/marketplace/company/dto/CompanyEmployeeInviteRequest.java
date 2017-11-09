package com.marketplace.company.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

@Data
public class CompanyEmployeeInviteRequest extends ResourceSupport {
    @NotBlank(message = "email is mandatory")
    private final String email;
}
