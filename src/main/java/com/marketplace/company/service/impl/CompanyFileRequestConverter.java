package com.marketplace.company.service.impl;

import com.marketplace.storage.dto.BucketRequest;
import com.marketplace.storage.dto.FileRequest;
import com.marketplace.storage.dto.FileRequest.FileType;
import org.springframework.stereotype.Service;

@Service
class CompanyFileRequestConverter {

     public FileRequest getFileRequest(final String bucketId, final String filename, final String description, final FileType fileType) {
          return FileRequest.builder()
              .bucket(this.getBucketRequest(bucketId))
              .name(filename)
              .description(description)
              .fileType(fileType)
              .build();
     }

     private BucketRequest getBucketRequest(final String bucketId) {
          return BucketRequest.builder()
              .bucketId(bucketId)
              .bucketType(BucketRequest.BucketType.BROKER)
              .build();

     }
}
