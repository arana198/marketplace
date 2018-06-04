package com.marketplace.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"userId"})
@ToString(of = {"id"})
@Accessors(chain = true)
@Entity
@Table(name = "user_password_tokens")
public class UserPasswordTokenBO implements Serializable {

  private static final long serialVersionUID = 2780670648745098454L;

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "token", nullable = false)
  private String token;

  @Column(name = "created_ts", nullable = false)
  private LocalDateTime createdTs;
}
