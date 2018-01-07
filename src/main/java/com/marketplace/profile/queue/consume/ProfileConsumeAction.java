package com.marketplace.profile.queue.consume;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum ProfileConsumeAction {
    USER_CREATED("USER_CREATED"),
    COMPANY_ADMIN_USER_CREATED("COMPANY_ADMIN_USER_CREATED"),
    BROKER_USER_CREATED("BROKER_USER_CREATED"),
    USER_STATUS_UPDATED("USER_STATUS_UPDATED"),

    USER_PROFILE_CREATED("USER_PROFILE_CREATED"),
    USER_PROFILE_UPDATED("USER_PROFILE_UPDATED"),

    //BROKER
    BROKER_ADDED_TO_COMPANY("BROKER_ADDED_TO_COMPANY"),
    BROKER_UPDATED_COMPANY("BROKER_ADDED_TO_COMPANY");

    private final String value;

    ProfileConsumeAction(final String value) {
        this.value = value;
    }

    public static Optional<ProfileConsumeAction> getActionFromString(final String value) {
        return Stream.of(ProfileConsumeAction.values())
                .filter(a -> a.getValue().equalsIgnoreCase(value))
                .findAny();
    }

    public static String getStringFromAction(final ProfileConsumeAction value) {
        return Optional.ofNullable(value)
                .map(ProfileConsumeAction::getValue)
                .orElse(null);
    }
}
