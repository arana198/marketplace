package com.marketplace.location.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.location.domain.CityBO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CityRepository extends BaseRepository<CityBO, String> {

  Optional<CityBO> findByName(String name);
}
