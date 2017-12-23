package com.marketplace.storage.domain;

import com.marketplace.common.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "file_store_permission")
public class BucketPermissionBO extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "bucket_id", referencedColumnName = "id", nullable = false)
    private BucketBO bucketBO;

    @Column(name = "user_id")
    private String userId;
}
