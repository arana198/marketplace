package com.marketplace.company.domain;

import com.marketplace.common.domain.AbstractTimestampEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "services")
public class ServiceBO extends AbstractTimestampEntity {

     @Column(name = "name", nullable = false)
     private String name;

     @Column(name = "display_name", nullable = false)
     private String displayName;

     @Column(name = "description")
     private String description;

     @OneToOne
     @JoinColumn(name = "parent_id", referencedColumnName = "id")
     private ServiceBO parent;

     @Column(name = "is_active", nullable = false)
     private boolean active;
}
