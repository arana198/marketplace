package com.marketplace.user.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode(of = {"id", "name", "selectable"})
@ToString(of = {"id", "name"})
@Entity
@Table(name = "user_status")
public class UserStatusBO implements Serializable {

     private static final long serialVersionUID = 1815696712482274042L;

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
}
