package com.marketplace.storage.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

@Data
public class FileResponse extends BaseResponseDomain {
  private final String fileId;
  private final String name;
  private final String type;
  private final String format;
  private final byte[] file;
}
