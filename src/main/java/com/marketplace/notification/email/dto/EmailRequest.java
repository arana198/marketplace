package com.marketplace.notification.email.dto;

import lombok.Data;

@Data
public class EmailRequest {
  private final String fromAddress;
  private final String toAddress;
  private final String subject;
  private final String content;
}
