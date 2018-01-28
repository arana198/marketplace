package com.marketplace.location.domain;

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
@EqualsAndHashCode(of = {"address", "city", "state", "location"})
@Entity
@Table(name = "address")
public class AddressBO extends AbstractEntity {

    @Column(name = "address", nullable = false)
    private String address;

    @OneToOne
    @JoinColumn(name = "city_id", referencedColumnName = "id", nullable = false)
    private CityBO city;

    @OneToOne
    @JoinColumn(name = "state_id", referencedColumnName = "id", nullable = false)
    private StateBO state;

    @OneToOne
    @JoinColumn(name = "postcode_id", referencedColumnName = "id", nullable = false)
    private PostcodeBO postcode;
}
