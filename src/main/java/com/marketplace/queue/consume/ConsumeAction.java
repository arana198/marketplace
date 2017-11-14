package com.marketplace.queue.consume;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum ConsumeAction {
    USER_CREATED("USER_CREATED"),
    COMPANY_ADMIN_USER_CREATED("COMPANY_ADMIN_USER_CREATED"),
    BROKER_USER_CREATED("BROKER_USER_CREATED"),
    USER_STATUS_UPDATED("USER_STATUS_UPDATED"),
    VERIFY_EMAIL("VERIFY_EMAIL"),
    FORGOTTEN_PASSWORD("FORGOTTEN_PASSWORD"),
    USER_PROFILE_CREATED("USER_PROFILE_CREATED"),
    USER_PROFILE_UPDATED("USER_PROFILE_UPDATED"),
    COMPANY_CREATED("COMPANY_CREATED"),
    COMPANY_UPDATED("COMPANY_UPDATED"),
    INVITE_BROKER("INVITE_BROKER"),
    EMPLOYEE_ADDED_TO_COMPANY("EMPLOYEE_ADDED_TO_COMPANY"),
    EMPLOYEE_REMOVED_FROM_COMPANY("EMPLOYEE_REMOVED_FROM_COMPANY");

    private final String value;

    ConsumeAction(final String value) {
        this.value = value;
    }

    public static ConsumeAction getActionFromString(final String value) {
        return Stream.of(ConsumeAction.values())
                .filter(a -> a.getValue().equalsIgnoreCase(value))
                .findAny()
                .orElse(null);
    }

    public static String getStringFromAction(final ConsumeAction value) {
        return Optional.ofNullable(value)
                .map(ConsumeAction::getValue)
                .orElse(null);
    }
}
