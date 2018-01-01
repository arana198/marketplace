package com.marketplace.company.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "broker_profiles")
public class BrokerProfileBO extends AbstractAuditEntity {

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "is_admin", nullable = false)
    private boolean admin;

    @Column(name = "is_active", nullable = false)
    private boolean active;
}
