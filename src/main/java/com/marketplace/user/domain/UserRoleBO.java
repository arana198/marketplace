package com.marketplace.user.domain;

import com.marketplace.common.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"user_id", "role_id"})
@Entity
@Table(name = "user_roles")
public class UserRoleBO implements Serializable {

    private static final long serialVersionUID = -5889526109417397633L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserBO user;

    @OneToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
    private RoleBO role;

    @OneToOne
    @JoinColumn(name = "user_status_id", referencedColumnName = "id", nullable = false)
    private UserStatusBO userStatus;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "provider_user_id", unique = true)
    private String providerUserId;

    @Column(name = "created_ts", nullable = false)
    private LocalDateTime createdTs;
}
