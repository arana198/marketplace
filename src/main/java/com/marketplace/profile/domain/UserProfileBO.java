package com.marketplace.profile.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"userId"})
@Entity
@Table(name = "user_profiles", indexes = {
    @Index(name = "ix_user_id", columnList = "user_id", unique = true)
})
public class UserProfileBO extends AbstractAuditEntity {

     @Column(name = "user_id", nullable = false)
     private String userId;

     @Column(name = "email", nullable = false)
     private String email;

     @Column(name = "firstname", nullable = false)
     private String firstName;

     @Column(name = "lastname", nullable = false)
     private String lastName;

     @Column(name = "mobile_number", nullable = false)
     private String mobileNumber;

     @Column(name = "dob", nullable = false)
     private LocalDate dateOfBirth;

     @Column(name = "location")
     private String postcode;
}
