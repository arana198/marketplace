package com.marketplace.storage.domain;

import com.marketplace.common.domain.AbstractTimestampEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "file_stores")
public class FileStoreBO extends AbstractTimestampEntity {

     @Column(name = "name", nullable = false)
     private String name;

     @Column(name = "description")
     private String description;

     @Column(name = "type", nullable = false)
     private String type;

     @Column(name = "format", nullable = false)
     private String format;

     @Lob
     @Basic(fetch = FetchType.LAZY)
     @Column(name = "data", nullable = false)
     private byte[] data;
}
