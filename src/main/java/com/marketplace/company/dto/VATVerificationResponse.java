package com.marketplace.company.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VATVerificationResponse {

  public final boolean valid;
  public final Name name;

  @JsonCreator
  public VATVerificationResponse(@JsonProperty("valid") final boolean valid,
                                 @JsonProperty("name") final Name name) {
    this.valid = valid;
    this.name = name;
  }

  @Data
  public static final class Name {

    private final String name;

    @JsonCreator
    public Name(@JsonProperty("0") final String name) {
      this.name = name;
    }
  }
}