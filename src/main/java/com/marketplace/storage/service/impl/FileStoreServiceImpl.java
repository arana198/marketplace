package com.marketplace.storage.service.impl;

import com.marketplace.storage.domain.FileStoreBO;
import com.marketplace.storage.dto.FileRequest;
import com.marketplace.storage.dto.FileResponse;
import com.marketplace.storage.service.FileStoreService;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Data
@Service
class FileStoreServiceImpl implements FileStoreService {

    private final FileStoreRepository fileStoreRepository;
    private final FileRequestConverter fileRequestConverter;
    private final FileReponseConverter fileReponseConverter;

    @Override
    public FileResponse store(final FileRequest documentRequest) throws IOException {
        final FileStoreBO fileStoreBO = fileRequestConverter.convert(documentRequest);
        fileStoreRepository.save(fileStoreBO);
        return fileReponseConverter.convert(fileStoreBO);
    }

    @Override
    public Optional<FileResponse> findById(final String id) {
        return fileStoreRepository.findById(id)
                .map(file -> {
                    try {
                        return fileReponseConverter.convert(file);
                    } catch (IOException e) {
                        return null;
                    }
                });
    }

    //TODO: Do you need document management service to manage documents for user/broker/application?
}
