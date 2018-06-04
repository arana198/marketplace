package com.marketplace.notification.email.domain;

import com.marketplace.common.converter.JpaJsonConverter;
import com.marketplace.common.domain.AbstractTimestampEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "email_notifications")
public class EmailNotificationBO extends AbstractTimestampEntity {

  @Column(name = "sent_to", nullable = false)
  private String sentTo;

  @Convert(converter = JpaJsonConverter.class)
  @Column(name = "email", columnDefinition = "json", nullable = false)
  private Object emailBO;
}
