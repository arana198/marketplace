package com.marketplace.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data
@EqualsAndHashCode(of = {"createdAt", "updatedAt"})
@ToString(of = {"createdAt", "updatedAt"})
@Accessors(chain = true)
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractEntity implements Serializable {

    private static final long serialVersionUID = 6384069660089559035L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "created_ts", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_ts", nullable = false)
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }

        updatedAt = createdAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}