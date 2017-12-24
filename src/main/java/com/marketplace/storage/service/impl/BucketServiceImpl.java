package com.marketplace.storage.service.impl;

import com.marketplace.company.dto.BrokerProfileResponse;
import com.marketplace.storage.domain.BucketBO;
import com.marketplace.storage.domain.BucketPermissionBO;
import com.marketplace.storage.dto.BucketPermissionResponse;
import com.marketplace.storage.dto.BucketRequest;
import com.marketplace.storage.dto.BucketResponse;
import com.marketplace.storage.queue.publish.StoragePublishService;
import com.marketplace.storage.queue.publish.domain.StoragePublishAction;
import com.marketplace.storage.service.BucketService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Service
class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;
    private final BucketRequestConverter bucketRequestConverter;
    private final BucketReponseConverter bucketReponseConverter;
    private final StoragePublishService publishService;

    @Override
    public BucketResponse getOrCreate(final BucketRequest bucketRequest) {
        final BucketBO bucketBO = bucketRepository.findById(bucketRequest.getBucketId())
                .orElseGet(() -> {
                    final BucketBO newBucket = bucketRequestConverter.convert(bucketRequest);
                    bucketRepository.save(newBucket);
                    publishService.sendMessage(StoragePublishAction.BUCKET_CREATED, bucketReponseConverter.convert(newBucket));
                    return newBucket;
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

    @Override
    public void addPermissions(final BucketPermissionResponse bucketPermissionResponse) {
        bucketRepository.findById(bucketPermissionResponse.getBucketId())
                .ifPresent(bucket -> {
                    final Set<BucketPermissionBO> bucketPermissions = bucketPermissionResponse.getBrokerProfiles()
                            .parallelStream()
                            .map(BrokerProfileResponse::getUserId)
                            .map(userId -> this.createBucketPermissionBO(bucket, userId))
                            .collect(Collectors.toSet());

                    bucket.getPermissions().addAll(bucketPermissions);
                    bucketRepository.save(bucket);
                });
    }

    private BucketPermissionBO createBucketPermissionBO(final BucketBO bucketBO, final String userId) {
        return new BucketPermissionBO()
                .setBucketBO(bucketBO)
                .setUserId(userId);
    }
}
