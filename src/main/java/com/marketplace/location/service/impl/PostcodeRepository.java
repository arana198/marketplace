package com.marketplace.location.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.location.domain.PostcodeBO;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface PostcodeRepository extends BaseRepository<PostcodeBO, String> {

  Optional<PostcodeBO> findByPostcode(String postcode);
}
