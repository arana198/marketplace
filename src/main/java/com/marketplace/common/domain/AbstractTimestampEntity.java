package com.marketplace.common.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
@MappedSuperclass
public abstract class AbstractTimestampEntity extends AbstractEntity {

     @Column(name = "created_ts", nullable = false)
     private LocalDate createdAt;

     @Column(name = "updated_ts", nullable = false)
     private LocalDate updatedAt;

     @PrePersist
     protected void onCreate() {
          super.onCreate();
          updatedAt = createdAt = LocalDate.now();
     }

     @PreUpdate
     protected void onUpdate() {
          updatedAt = LocalDate.now();
     }

     public void update(final AbstractTimestampEntity abstractEntity) {
          this.setId(abstractEntity.getId());
          this.setCreatedAt(abstractEntity.getCreatedAt());
          this.setUpdatedAt(abstractEntity.getUpdatedAt());
     }
}