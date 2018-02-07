package com.marketplace.common.domain;

import com.marketplace.common.security.AuthUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"createdAt", "updatedBy", "updatedAt"})
@ToString(of = {"createdAt", "updatedBy", "updatedAt"})
@Accessors(chain = true)
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractAuditEntity extends AbstractEntity {

    @Transient
    private SecurityContext securityContext = SecurityContextHolder.getContext();

    @Column(name = "created_ts", nullable = false)
    private LocalDateTime createdAt;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @Column(name = "updated_ts", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        updatedAt = createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        if (securityContext.getAuthentication() != null) {
            updatedBy = AuthUser.getUserId();
        }
    }

    public void update(AbstractAuditEntity abstractAuditEntity) {
        this.setId(abstractAuditEntity.getId());
        this.setCreatedAt(abstractAuditEntity.getCreatedAt());
        this.setUpdatedAt(abstractAuditEntity.getUpdatedAt());
        this.setUpdatedBy(abstractAuditEntity.getUpdatedBy());
        this.setVersion(abstractAuditEntity.getVersion());
    }
}