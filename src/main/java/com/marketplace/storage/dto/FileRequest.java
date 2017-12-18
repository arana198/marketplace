package com.marketplace.storage.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileRequest {
    private final String name;
    private final String description;
    private final MultipartFile file;
    private final boolean show;
}
