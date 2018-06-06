package com.marketplace.storage.service;

import com.marketplace.storage.dto.BucketPermissionResponse;
import com.marketplace.storage.dto.BucketRequest;
import com.marketplace.storage.dto.BucketResponse;

public interface BucketService {
     BucketResponse getOrCreate(BucketRequest bucketRequest);

     boolean checkIfUserHasPermission(String bucketId, String userId);

     void addPermissions(BucketPermissionResponse bucketPermissionResponse);
}
