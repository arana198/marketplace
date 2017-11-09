package com.marketplace.broker.profile.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

@Data
public class CompanyEmployeeRequest extends ResourceSupport {
    @NotBlank(message = "isAdmin is mandatory")
    private final boolean isAdmin;
}
