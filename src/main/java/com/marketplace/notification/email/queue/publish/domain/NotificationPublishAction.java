package com.marketplace.notification.email.queue.publish.domain;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum NotificationPublishAction {
     USER_CREATED("USER_CREATED"),
     COMPANY_ADMIN_USER_CREATED("COMPANY_ADMIN_USER_CREATED"),
     BROKER_USER_CREATED("BROKER_USER_CREATED"),
     USER_STATUS_UPDATED("USER_STATUS_UPDATED"),

     VERIFY_EMAIL("VERIFY_EMAIL"),
     EMAIL_VERIFIED("EMAIL_VERIFIED"),
     FORGOTTEN_PASSWORD("FORGOTTEN_PASSWORD"),
     USER_PROFILE_CREATED("USER_PROFILE_CREATED"),
     USER_PROFILE_UPDATED("USER_PROFILE_UPDATED");


     private final String value;

     NotificationPublishAction(final String value) {
          this.value = value;
     }

     public static NotificationPublishAction getActionFromString(final String value) {
          return Stream.of(NotificationPublishAction.values())
              .filter(a -> a.getValue().equalsIgnoreCase(value))
              .findAny()
              .orElse(null);
     }

     public static String getStringFromAction(final NotificationPublishAction value) {
          return Optional.ofNullable(value)
              .map(NotificationPublishAction::getValue)
              .orElse(null);
     }
}
