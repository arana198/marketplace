package com.marketplace.user.domain;

import com.marketplace.common.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true, of = {"user_id", "role_id"})
@Entity
@Table(name = "user_roles")
public class UserRoleBO extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -5889526109417397633L;

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
}
