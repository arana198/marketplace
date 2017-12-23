package com.marketplace.storage.service.impl;

import com.marketplace.storage.domain.BucketBO;
import com.marketplace.storage.dto.BucketRequest;
import com.marketplace.storage.dto.BucketResponse;
import com.marketplace.storage.service.BucketService;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;
    private final BucketRequestConverter bucketRequestConverter;
    private final BucketReponseConverter bucketReponseConverter;

    @Override
    public BucketResponse getOrCreate(final BucketRequest bucketRequest) {
        final BucketBO bucketBO = bucketRepository.findById(bucketRequest.getBucketId())
                .orElseGet(() -> {
                    final BucketBO newBucket = bucketRequestConverter.convert(bucketRequest);
                    return bucketRepository.save(newBucket);
                });

        return bucketReponseConverter.convert(bucketBO);
    }

    @Override
    public boolean checkIfUserHasPermission(final String bucketId, final String userId) {
        return bucketRepository.findById(bucketId)
                .filter(bucketBO -> bucketBO.getPermissions()
                        .parallelStream()
                        .anyMatch(bp -> bp.getUserId().equalsIgnoreCase(userId)))
                .isPresent();
    }
}
