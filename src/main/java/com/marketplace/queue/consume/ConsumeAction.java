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
    EMAIL_VERIFIED("EMAIL_VERIFIED"),
    FORGOTTEN_PASSWORD("FORGOTTEN_PASSWORD"),
    USER_PROFILE_CREATED("USER_PROFILE_CREATED"),
    USER_PROFILE_UPDATED("USER_PROFILE_UPDATED"),

    //COMPANY
    COMPANY_CREATED("COMPANY_CREATED"),
    COMPANY_UPDATED("COMPANY_UPDATED"),

    //BROKER
    INVITE_BROKER("INVITE_BROKER"),
    BROKER_ADDED_TO_COMPANY("BROKER_ADDED_TO_COMPANY"),
    BROKER_UPDATED_COMPANY("BROKER_ADDED_TO_COMPANY"),
    BROKER_REMOVED_FROM_COMPANY("BROKER_REMOVED_FROM_COMPANY"),
    BROKER_REMOVED_AS_ADMIN("BROKER_REMOVED_AS_ADMIN"),
    BROKER_ADDED_AS_ADMIN("BROKER_ADDED_AS_ADMIN"),

    //DOCUMENT
    BUCKET_CREATED("BUCKET_CREATED"),
    COMPANY_BROKERS("COMPANY_BROKERS");

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
