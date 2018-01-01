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
@Table(name = "broker_documents")
public class BrokerDocumentBO extends AbstractAuditEntity {

    @Column(name = "broker_profile_id", nullable = false)
    private String brokerProfileId;

    @Column(name = "file_id", nullable = false)
    private String fileId;

    @Column(name = "is_verified", nullable = false)
    private boolean verified;
}
