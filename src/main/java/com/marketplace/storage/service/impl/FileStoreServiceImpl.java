package com.marketplace.storage.service.impl;

import com.marketplace.storage.domain.BucketFilesBO;
import com.marketplace.storage.domain.FileStoreBO;
import com.marketplace.storage.dto.BucketResponse;
import com.marketplace.storage.dto.FileRequest;
import com.marketplace.storage.dto.FileRequest.FileType;
import com.marketplace.storage.dto.FileResponse;
import com.marketplace.storage.service.BucketService;
import com.marketplace.storage.service.FileStoreService;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Data
@Service
class FileStoreServiceImpl implements FileStoreService {

    private final FileStoreRepository fileStoreRepository;
    private final BucketFileRepository bucketFileRepository;
    private final FileRequestConverter fileRequestConverter;
    private final FileReponseConverter fileReponseConverter;
    private final BucketService bucketService;

    @Transactional
    @Override
    public FileResponse store(final FileRequest fileRequest, final MultipartFile file) throws IOException {
        final BucketResponse bucketResponse = bucketService.getOrCreate(fileRequest.getBucket());
        final FileStoreBO fileStoreBO = fileRequestConverter.convert(fileRequest, file);
        final BucketFilesBO bucketFileBO = this.createBucketFileBO(bucketResponse.getBucketId(), fileStoreBO, fileRequest.getFileType());

        fileStoreRepository.save(fileStoreBO);
        bucketFileRepository.save(bucketFileBO);
        return fileReponseConverter.convert(fileStoreBO);
    }

    @Override
    public Optional<FileResponse> findById(final String userId, final String fileId) {
        return bucketFileRepository.findByFileId(fileId)
                .filter(bucketFilesBO -> bucketFilesBO.isUnrestricted() || bucketService.checkIfUserHasPermission(bucketFilesBO.getBucketId(), userId))
                .map(BucketFilesBO::getFileStore)
                .map(file -> {
                    try {
                        return fileReponseConverter.convert(file);
                    } catch (IOException e) {
                        return null;
                    }
                });
    }

    private BucketFilesBO createBucketFileBO(final String bucketId, final FileStoreBO fileStoreBO, final FileType fileType) {
        return new BucketFilesBO()
                .setBucketId(bucketId)
                .setFileStore(fileStoreBO)
                .setUnrestricted(fileType.isPublic());
    }
}
