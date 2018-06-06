package com.marketplace.location.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.location.domain.OutcodeBO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface OutcodeRepository extends BaseRepository<OutcodeBO, String> {

     Optional<OutcodeBO> findByOutcode(String outcode);
}
