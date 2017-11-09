package com.marketplace.company.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
public class CompanyEmployeeResponse extends ResourceSupport {
    private final String companyId;
    private final BrokerProfileResponse user;
    private final String isAdmin;
}
