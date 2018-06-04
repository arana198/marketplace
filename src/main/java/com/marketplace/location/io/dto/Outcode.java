package com.marketplace.location.io.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Outcode {
  private final String outcode;
  private final float longitude;
  private final float latitude;
}
