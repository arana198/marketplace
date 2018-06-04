package com.marketplace.location.io.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Postcode {
  private final String postcode;
  private final Outcode outcode;
  private final float longitude;
  private final float latitude;
}
