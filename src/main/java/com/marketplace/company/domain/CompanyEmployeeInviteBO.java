package com.marketplace.company.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "company_employee_invite")
public class CompanyEmployeeInviteBO implements Serializable {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "company_id", nullable = false)
  private String companyId;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "token", nullable = false)
  private String token;

  @Column(name = "created_ts", nullable = false)
  private LocalDate createdAt;
}
