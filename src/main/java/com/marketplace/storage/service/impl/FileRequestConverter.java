package com.marketplace.storage.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.common.exception.BadRequestException;
import com.marketplace.storage.compress.CompressorUtils;
import com.marketplace.storage.dto.FileRequest;
import com.marketplace.storage.domain.FileStoreBO;
import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
class FileRequestConverter implements BaseConverter<FileRequest, FileStoreBO> {
    @Override
    public FileStoreBO convert(final FileRequest source) {
        try {
            return new FileStoreBO()
                    .setName(source.getName())
                    .setDescription(source.getDescription())
                    .setType(source.getFile().getContentType())
                    .setData(CompressorUtils.compress(source.getFile().getBytes()));
        } catch (IOException e) {
            throw new BadRequestException("Could not read file");
        }
    }
}
