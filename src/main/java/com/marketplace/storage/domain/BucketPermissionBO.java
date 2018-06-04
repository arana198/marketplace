package com.marketplace.storage.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(of = {"bucketBO", "userId"})
@Entity
@Table(name = "bucket_permissions")
public class BucketPermissionBO extends AbstractAuditEntity {

  @ManyToOne
  @JoinColumn(name = "bucket_id", referencedColumnName = "id", nullable = false)
  private BucketBO bucketBO;

  @Column(name = "user_id", nullable = false)
  private String userId;
}
