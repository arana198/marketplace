package com.marketplace.user.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true, of = {"username"})
@Entity
@Table(name = "users")
public class UserBO extends AbstractAuditEntity implements Serializable {

    private static final long serialVersionUID = -5889526109417397633L;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @NotAudited
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<UserRoleBO> roles = new HashSet<>();
}
