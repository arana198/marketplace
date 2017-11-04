package com.marketplace.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"name"})
@Accessors(chain = true)
@Entity
@Table(name = "roles")
public class RoleBO implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = -2934253177419534374L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_selectable", nullable = false)
    private boolean selectable;

    @Override
    public String getAuthority() {
        return name;
    }

}
