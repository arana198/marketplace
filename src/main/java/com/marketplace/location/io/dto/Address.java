package com.marketplace.location.io.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Address {
  private final List<String> houseNumber;
  private final String streetName;
  private final String city;
  private final String state;
  private final Postcode postcode;
}
