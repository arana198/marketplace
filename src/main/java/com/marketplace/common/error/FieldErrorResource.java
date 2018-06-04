package com.marketplace.common.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;
import org.springframework.validation.FieldError;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldErrorResource {
  private final String resource;
  private final String field;
  private final String code;
  private final String message;

  public static FieldErrorResource of(final FieldError fieldError) {
    return new FieldErrorResource(
        fieldError.getObjectName(),
        fieldError.getField(),
        fieldError.getCode(),
        fieldError.getDefaultMessage());
  }
}
