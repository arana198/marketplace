package com.marketplace.company.domain;

import com.marketplace.common.domain.AbstractEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "advice_methods")
public class AdviceMethodBO extends AbstractEntity {

     @Column(name = "name", nullable = false)
     private String name;

     @Column(name = "display_name", nullable = false)
     private String displayName;

     @Column(name = "description")
     private String description;

     @Column(name = "is_active", nullable = false)
     private boolean active;
}
