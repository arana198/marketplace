package com.marketplace.storage.service.impl;


import com.marketplace.common.repository.BaseRepository;
import com.marketplace.storage.domain.BucketFilesBO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface BucketFileRepository extends BaseRepository<BucketFilesBO, String> {

  @Query("select bf from BucketFilesBO bf where bf.fileStore.id = :fileId")
  Optional<BucketFilesBO> findByFileId(@Param("fileId") String fileId);
}
