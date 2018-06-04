package com.marketplace.storage.domain;

import com.marketplace.common.domain.AbstractAuditEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true, of = {"bucketId", "fileStore"})
@Entity
@Table(name = "bucket_files")
public class BucketFilesBO extends AbstractAuditEntity {

  @Column(name = "bucket_id", nullable = false)
  private String bucketId;

  @OneToOne
  @JoinColumn(name = "file_store_id", referencedColumnName = "id", nullable = false)
  private FileStoreBO fileStore;

  @Column(name = "is_public", nullable = false)
  private boolean unrestricted = false;
}
