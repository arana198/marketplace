package com.marketplace.location.domain;

import com.marketplace.common.domain.AbstractEntity;
import com.vividsolutions.jts.geom.Point;
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
@EqualsAndHashCode(of = {"location"})
@Entity
@Table(name = "location")
public class PostcodeBO extends AbstractEntity {

  @Column(name = "location", nullable = false)
  private String postcode;

  @OneToOne
  @JoinColumn(name = "outcode_id", referencedColumnName = "id", nullable = false)
  private OutcodeBO outcode;

  @Column(name = "coordinates", columnDefinition = "geometry", nullable = false)
  private Point coordinates;

  @Column(name = "is_active", nullable = false)
  private boolean active;
}
