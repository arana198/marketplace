package com.marketplace.queue.publish.domain;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum PublishAction {
    USER_CREATED("USER_CREATED"),
    USER_UPDATED("USER_UPDATED"),
    USER_STATUS_UPDATED("USER_STATUS_UPDATED"),
    USER_PROFILE_CREATED("USER_PROFILE_CREATED"),
    USER_PROFILE_UPDATED("USER_PROFILE_UPDATED"),
    COMPANY_CREATED("COMPANY_CREATED"),
    COMPANY_UPDATED("COMPANY_UPDATED");

    private final String value;

    PublishAction(final String value) {
        this.value = value;
    }

    public static PublishAction getActionFromString(final String value) {
        return Stream.of(PublishAction.values())
                .filter(a -> a.getValue().equalsIgnoreCase(value))
                .findAny()
                .orElse(null);
    }

    public static String getStringFromAction(final PublishAction value) {
        return Optional.ofNullable(value)
                .map(PublishAction::getValue)
                .orElse(null);
    }
}
