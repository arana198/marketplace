package com.marketplace.company.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"brokerProfileId"})
@Entity
@Table(name = "company_validators")
public class CompanyValidatorBO extends AbstractAuditEntity {

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "fca_number_verified", nullable = false)
    private boolean fcaNumberVerified = false;

    @Column(name = "billing_active", nullable = false)
    private boolean billingActive = false;

    @Column(name = "fca_number_verified_ts", nullable = false)
    private LocalDateTime fcaNumberVerifiedAt;
}
