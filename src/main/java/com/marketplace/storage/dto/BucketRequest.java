package com.marketplace.storage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
@JsonDeserialize(builder = BucketRequest.BucketRequestBuilder.class)
@JsonIgnoreProperties
public class BucketRequest {

  @NotNull(message = "bucketId is mandatory")
  private final String bucketId;

  @NotNull(message = "bucketType is mandatory")
  private final BucketType bucketType;

  public enum BucketType {
    BROKER("BROKER"),
    COMPANY("COMPANY"),
    APPLICATION("APPLICATION"),
    SYSTEM("SYSTEM");

    private String value;
    private boolean unrestricted;

    BucketType(final String value) {
      this.value = value;
    }

    public static BucketType getRoleFromString(final String value) {
      for (BucketType bucketType : BucketType.values()) {
        if (bucketType.getValue().equalsIgnoreCase(value)) {
          return bucketType;
        }
      }

      return null;
    }

    public String getValue() {
      return this.value;
    }
  }

  @JsonPOJOBuilder(withPrefix = "")
  public static class BucketRequestBuilder {
  }
}
