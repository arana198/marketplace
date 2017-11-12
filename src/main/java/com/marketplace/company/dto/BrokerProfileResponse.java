package com.marketplace.company.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
public class BrokerProfileResponse extends ResourceSupport {
    private final String companyId;
    private final String isAdmin;
}
