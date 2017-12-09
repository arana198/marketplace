package com.marketplace.user.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.envers.NotAudited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, of = {"username"})
@Entity
@Table(name = "users")
public class UserBO extends AbstractAuditEntity {

    private static final long serialVersionUID = -5889526109417397633L;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @NotAudited
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<UserRoleBO> roles = new HashSet<>();

    @Column(name = "is_email_verified")
    private boolean emailVerified;

    public void removeRole(UserRoleBO userRoleBO) {
        this.roles.remove(userRoleBO);
        userRoleBO.setUser(null);
    }

}
