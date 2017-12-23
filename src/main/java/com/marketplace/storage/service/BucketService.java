package com.marketplace.storage.service;

import com.marketplace.storage.dto.BucketRequest;
import com.marketplace.storage.dto.BucketResponse;
import com.marketplace.storage.dto.FileRequest;
import com.marketplace.storage.dto.FileResponse;

import java.io.IOException;
import java.util.Optional;

public interface BucketService {
    BucketResponse getOrCreate(BucketRequest bucketRequest);

    boolean checkIfUserHasPermission(String bucketId, String userId);
}
