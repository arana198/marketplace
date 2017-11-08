package com.marketplace.broker.profile.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
import com.marketplace.common.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "company")
public class CompanyBO extends AbstractAuditEntity implements Serializable {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "company_number", nullable = false)
    private String companyNumber;

    @Column(name = "vat_number", nullable = false)
    private String vatNumber;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "is_active", nullable = false)
    private boolean active;
}
