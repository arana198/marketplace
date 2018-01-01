package com.marketplace.company.queue.consume;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum CompanyConsumeAction {

    //USER
    USER_STATUS_UPDATED("USER_STATUS_UPDATED"),

    //COMPANY
    COMPANY_ACTIVATED("COMPANY_ACTIVATED"),
    COMPANY_INACTIVATED("COMPANY_INACTIVATED"),

    //DOCUMENT
    BROKER_CERTIFICATE_VERIFIED("BROKER_CERTIFICATE_VERIFIED"),
    BUCKET_CREATED("BUCKET_CREATED");

    private final String value;

    CompanyConsumeAction(final String value) {
        this.value = value;
    }

    public static CompanyConsumeAction getActionFromString(final String value) {
        return Stream.of(CompanyConsumeAction.values())
                .filter(a -> a.getValue().equalsIgnoreCase(value))
                .findAny()
                .orElse(null);
    }

    public static String getStringFromAction(final CompanyConsumeAction value) {
        return Optional.ofNullable(value)
                .map(CompanyConsumeAction::getValue)
                .orElse(null);
    }
}
