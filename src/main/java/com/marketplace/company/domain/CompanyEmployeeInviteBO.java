package com.marketplace.company.domain;

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
@Table(name = "company_employee_invite")
public class CompanyEmployeeInviteBO extends AbstractEntity implements Serializable {

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "token", nullable = false)
    private String token;
}
