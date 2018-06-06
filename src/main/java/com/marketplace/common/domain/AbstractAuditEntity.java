package com.marketplace.common.domain;

import com.marketplace.common.security.AuthUser;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RevisionTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@MappedSuperclass
@Audited
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractAuditEntity extends AbstractEntity {

     @NotAudited
     @Column(name = "created_ts", nullable = false)
     private LocalDateTime createdAt;

     @RevisionTimestamp
     @Column(name = "updated_ts", nullable = false)
     private LocalDateTime updatedAt;

     @Column(name = "updated_by", nullable = false)
     private String updatedBy;

     @PrePersist
     protected void onCreate() {
          super.onCreate();
          updatedAt = createdAt = LocalDateTime.now();
     }

     @PreUpdate
     protected void onUpdate() {
          updatedAt = LocalDateTime.now();
          updatedBy = AuthUser.getUserId();
     }

     public void update(final AbstractAuditEntity abstractAuditEntity) {
          this.setId(abstractAuditEntity.getId());
          this.setCreatedAt(abstractAuditEntity.getCreatedAt());
          this.setUpdatedAt(abstractAuditEntity.getUpdatedAt());
          this.setUpdatedBy(abstractAuditEntity.getUpdatedBy());
          this.setVersion(abstractAuditEntity.getVersion());
     }
}