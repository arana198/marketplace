package com.marketplace.common.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;

public class JpaJsonConverter implements AttributeConverter<Object, String> {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(final Object meta) {
    try {
      return OBJECT_MAPPER.writeValueAsString(meta);
    } catch (JsonProcessingException ex) {
      return null;
    }
  }

  @Override
  public Object convertToEntityAttribute(final String dbData) {
    try {
      return OBJECT_MAPPER.readValue(dbData, Object.class);
    } catch (IOException ex) {
      // logger.error("Unexpected IOEx decoding json from database: " + dbData);
      return null;
    }
  }
}
