package com.marketplace.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

  @Override
  public Date convertToDatabaseColumn(final LocalDate date) {
    if (date == null) {
      return null;
    }
    return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  @Override
  public LocalDate convertToEntityAttribute(final Date value) {
    if (value == null) {
      return null;
    }
    Instant instant = value.toInstant();
    ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
    return zdt.toLocalDate();
  }
}
