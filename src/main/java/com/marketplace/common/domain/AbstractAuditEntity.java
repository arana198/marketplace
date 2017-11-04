package com.marketplace.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = {"createdAt", "updatedBy", "updatedAt"})
@ToString(of = {"createdAt", "updatedBy", "updatedAt"})
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractAuditEntity implements Serializable {

    private static final long serialVersionUID = 6384069660089559035L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Transient
    private SecurityContext securityContext = SecurityContextHolder.getContext();

    @Column(name = "created_ts", nullable = false)
    private LocalDate createdTs;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "updated_ts", nullable = false)
    private LocalDate updatedTs;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @PrePersist
    protected void onCreate() {
        id = UUID.randomUUID().toString();
        updatedTs = createdTs = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTs = LocalDate.now();
        if (securityContext.getAuthentication() != null) {
            String updateByUserId = securityContext.getAuthentication().getName();
            updatedBy = updateByUserId;
        }
    }
}