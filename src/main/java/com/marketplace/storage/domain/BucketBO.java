package com.marketplace.storage.domain;

import com.marketplace.common.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "bucket")
public class BucketBO extends AbstractEntity {

    @Column(name = "type", nullable = false)
    private String type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "bucket_id")
    private Set<BucketPermissionBO> permissions = new HashSet<>();
}
