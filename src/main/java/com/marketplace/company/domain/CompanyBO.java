package com.marketplace.company.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "companies")
public class CompanyBO extends AbstractAuditEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "company_number", nullable = false)
    private String companyNumber;

    @Column(name = "vat_number", nullable = false)
    private String vatNumber;

    @Column(name = "fca_number", nullable = false)
    private String fcaNumber;

    @Column(name = "fca_number_verified", nullable = false)
    private boolean fcaNumberVerified = false;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    //TODO: Billing inactive company inactive
}
