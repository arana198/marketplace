package com.marketplace.location.dto;

import com.marketplace.common.dto.BaseResponseDomain;
import lombok.Data;

@Data
public class AddressResponse extends BaseResponseDomain {
  private final String addressId;
  private final String addressLine1;
  private final String addressLine2;
  private final String city;
  private final String state;
  private final String postcode;
}
