package com.marketplace.notification.email.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.notification.email.domain.EmailBO;
import com.marketplace.notification.email.dto.EmailRequest;
import org.springframework.stereotype.Service;

@Service
class EmailRequestConverter implements BaseConverter<EmailRequest, EmailBO> {
  @Override
  public EmailBO convert(final EmailRequest source) {
    return new EmailBO()
        .setFromAddress(source.getFromAddress())
        .setContent(source.getContent())
        .setSubject(source.getSubject())
        .setToAddress(source.getToAddress());
  }
}
