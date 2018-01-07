package com.marketplace.storage.service.impl;


import com.marketplace.common.repository.BaseRepository;
import com.marketplace.storage.domain.BucketBO;
import org.springframework.stereotype.Repository;

@Repository
interface BucketRepository extends BaseRepository<BucketBO, String> {

}
