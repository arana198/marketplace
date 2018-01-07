package com.marketplace.notification.email.queue.consume;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum NotificationConsumeAction {

    VERIFY_EMAIL("VERIFY_EMAIL"),
    FORGOTTEN_PASSWORD("FORGOTTEN_PASSWORD"),

    //BROKER
    INVITE_BROKER("INVITE_BROKER");

    private final String value;

    NotificationConsumeAction(final String value) {
        this.value = value;
    }

    public static Optional<NotificationConsumeAction> getActionFromString(final String value) {
        return Stream.of(NotificationConsumeAction.values())
                .filter(a -> a.getValue().equalsIgnoreCase(value))
                .findAny();
    }

    public static String getStringFromAction(final NotificationConsumeAction value) {
        return Optional.ofNullable(value)
                .map(NotificationConsumeAction::getValue)
                .orElse(null);
    }
}
