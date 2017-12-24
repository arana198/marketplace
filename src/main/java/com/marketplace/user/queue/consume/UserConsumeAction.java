package com.marketplace.user.queue.consume;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum UserConsumeAction {
    //BROKER
    BROKER_REMOVED_FROM_COMPANY("BROKER_REMOVED_FROM_COMPANY"),
    BROKER_REMOVED_AS_ADMIN("BROKER_REMOVED_AS_ADMIN"),
    BROKER_ADDED_AS_ADMIN("BROKER_ADDED_AS_ADMIN");

    private final String value;

    UserConsumeAction(final String value) {
        this.value = value;
    }

    public static UserConsumeAction getActionFromString(final String value) {
        return Stream.of(UserConsumeAction.values())
                .filter(a -> a.getValue().equalsIgnoreCase(value))
                .findAny()
                .orElse(null);
    }

    public static String getStringFromAction(final UserConsumeAction value) {
        return Optional.ofNullable(value)
                .map(UserConsumeAction::getValue)
                .orElse(null);
    }
}
