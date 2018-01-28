package com.marketplace.location.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.location.domain.AddressBO;
import com.marketplace.location.domain.PostcodeBO;
import com.marketplace.location.io.dto.Postcode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface AddressRepository extends BaseRepository<AddressBO, String> {

    List<AddressBO> findByPostcode(PostcodeBO postcode);

    @Query("SELECT a FROM AddressBO a WHERE a.postcode.postcode = :postcode")
    List<AddressBO> findByPostcode(@Param("location") String postcode);

}
