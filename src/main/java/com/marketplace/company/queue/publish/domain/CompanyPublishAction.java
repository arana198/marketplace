package com.marketplace.company.queue.publish.domain;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum CompanyPublishAction {
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
    COMPANY_BROKERS("COMPANY_BROKERS");


    private final String value;

    CompanyPublishAction(final String value) {
        this.value = value;
    }

    public static CompanyPublishAction getActionFromString(final String value) {
        return Stream.of(CompanyPublishAction.values())
                .filter(a -> a.getValue().equalsIgnoreCase(value))
                .findAny()
                .orElse(null);
    }

    public static String getStringFromAction(final CompanyPublishAction value) {
        return Optional.ofNullable(value)
                .map(CompanyPublishAction::getValue)
                .orElse(null);
    }
}
