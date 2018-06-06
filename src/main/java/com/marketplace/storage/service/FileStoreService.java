package com.marketplace.storage.service;

import com.marketplace.storage.dto.FileRequest;
import com.marketplace.storage.dto.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface FileStoreService {
     FileResponse store(FileRequest fileRequest, MultipartFile file) throws IOException;

     Optional<FileResponse> findById(String userId, String fileId);
}
