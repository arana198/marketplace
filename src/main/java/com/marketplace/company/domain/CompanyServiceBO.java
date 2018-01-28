package com.marketplace.company.domain;

import com.marketplace.common.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"companyId", "serviceId"})
@Entity
@Table(name = "company_services")
public class CompanyServiceBO extends AbstractEntity {

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "service_id", nullable = false)
    private String serviceId;
}
