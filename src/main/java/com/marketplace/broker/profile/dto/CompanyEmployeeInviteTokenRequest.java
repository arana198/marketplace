package com.marketplace.broker.profile.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

@Data
public class CompanyEmployeeInviteTokenRequest extends ResourceSupport {
    @NotBlank(message = "token is mandatory")
    private final String token;
}
