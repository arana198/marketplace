package com.marketplace.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"user"})
@ToString(of = {"id"})
@Entity
@Table(name = "user_password_tokens")
public class UserPasswordTokenBO implements Serializable {

    private static final long serialVersionUID = 2780670648745098454L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserBO user;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "created_ts", nullable = false)
    private LocalDateTime createdTs;
}
