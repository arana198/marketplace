package com.marketplace.company.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

@Data
public class CompanyResponse extends ResourceSupport {
    private final String companyId;
    private final String name;
    private final String companyNumber;
    private final String vatNumber;
    private final String logoUrl;
    private final String websiteUrl;
}
