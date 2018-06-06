package com.marketplace.company.domain;

import com.marketplace.common.domain.AbstractTimestampEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"companyId", "adviceId"})
@Entity
@Table(name = "company_advice_methods")
public class CompanyAdviceMethodBO extends AbstractTimestampEntity {

     @Column(name = "company_id", nullable = false)
     private String companyId;

     @Column(name = "advice_id", nullable = false)
     private String adviceId;
}
