package com.marketplace.storage.service;

import com.marketplace.storage.dto.FileRequest;
import com.marketplace.storage.dto.FileResponse;

import java.io.IOException;
import java.util.Optional;

public interface FileStoreService {
    FileResponse store(FileRequest documentRequest) throws IOException;

    Optional<FileResponse> findById(String id);
}
