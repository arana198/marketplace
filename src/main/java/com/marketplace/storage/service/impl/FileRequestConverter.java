package com.marketplace.storage.service.impl;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.storage.domain.FileStoreBO;
import com.marketplace.storage.dto.FileRequest;
import com.marketplace.storage.utils.CompressorUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
class FileRequestConverter {
  public FileStoreBO convert(final FileRequest source, final MultipartFile file) {
    try {
      return new FileStoreBO()
          .setName(source.getName())
          .setDescription(source.getDescription())
          .setType(source.getFileType().getValue())
          .setFormat(file.getContentType())
          .setData(CompressorUtils.compress(file.getBytes()));
    } catch (IOException e) {
      throw new BadRequestException("Could not read file");
    }
  }
}
