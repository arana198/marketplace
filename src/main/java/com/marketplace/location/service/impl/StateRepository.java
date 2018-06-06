package com.marketplace.location.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.location.domain.StateBO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface StateRepository extends BaseRepository<StateBO, String> {

     Optional<StateBO> findByName(String name);
}
