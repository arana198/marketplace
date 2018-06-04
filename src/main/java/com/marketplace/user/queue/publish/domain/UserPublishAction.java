package com.marketplace.user.queue.publish.domain;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum UserPublishAction {
  USER_CREATED("USER_CREATED"),
  COMPANY_ADMIN_USER_CREATED("COMPANY_ADMIN_USER_CREATED"),
  BROKER_USER_CREATED("BROKER_USER_CREATED"),
  USER_STATUS_UPDATED("USER_STATUS_UPDATED"),

  VERIFY_EMAIL("VERIFY_EMAIL"),
  EMAIL_VERIFIED("EMAIL_VERIFIED"),
  FORGOTTEN_PASSWORD("FORGOTTEN_PASSWORD");


  private final String value;

  UserPublishAction(final String value) {
    this.value = value;
  }

  public static UserPublishAction getActionFromString(final String value) {
    return Stream.of(UserPublishAction.values())
        .filter(a -> a.getValue().equalsIgnoreCase(value))
        .findAny()
        .orElse(null);
  }

  public static String getStringFromAction(final UserPublishAction value) {
    return Optional.ofNullable(value)
        .map(UserPublishAction::getValue)
        .orElse(null);
  }
}
