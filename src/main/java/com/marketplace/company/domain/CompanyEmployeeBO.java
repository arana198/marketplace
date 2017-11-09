package com.marketplace.company.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
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
@Table(name = "company_employee")
public class CompanyEmployeeBO extends AbstractAuditEntity implements Serializable {

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "is_admin", nullable = false)
    private boolean adminPrivilege;
}
