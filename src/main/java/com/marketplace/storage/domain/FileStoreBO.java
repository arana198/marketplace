package com.marketplace.storage.domain;

import com.marketplace.common.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
@Entity
@Table(name = "file_store")
public class FileStoreBO extends AbstractEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "data", nullable = false)
    private byte[] data;
}
