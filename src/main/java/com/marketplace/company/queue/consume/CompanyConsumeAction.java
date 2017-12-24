package com.marketplace.company.queue.consume;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum CompanyConsumeAction {
    //DOCUMENT
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
