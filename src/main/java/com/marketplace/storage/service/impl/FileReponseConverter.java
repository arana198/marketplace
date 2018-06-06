package com.marketplace.storage.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.storage.domain.FileStoreBO;
import com.marketplace.storage.dto.FileResponse;
import com.marketplace.storage.utils.CompressorUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
class FileReponseConverter implements BaseConverter<FileStoreBO, FileResponse> {

     @Override
     public FileResponse convert(final FileStoreBO source) throws IOException {
          return new FileResponse(
              source.getId(),
              source.getName(),
              source.getType(),
              source.getFormat(),
              CompressorUtils.decompress(source.getData()));
     }
}
