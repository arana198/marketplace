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
@EqualsAndHashCode(of = {"outcode"})
@Entity
@Table(name = "outcode")
public class OutcodeBO extends AbstractEntity {

     @Column(name = "outcode", nullable = false)
     private String outcode;

     @OneToOne
     @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
     private CityBO city;

     @Column(name = "coordinates", columnDefinition = "geometry", nullable = false)
     private Point coordinates;

     @Column(name = "is_active", nullable = false)
     private boolean active;
}
