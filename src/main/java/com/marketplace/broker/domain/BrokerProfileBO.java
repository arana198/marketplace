package com.marketplace.broker.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "broker_profile")
public class BrokerProfileBO extends AbstractAuditEntity implements Serializable {

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    //personal details - address
    //billing if independant
    //
}
