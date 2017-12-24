package com.marketplace.storage.queue.consume;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum StorageConsumeAction {
    COMPANY_BROKERS("COMPANY_BROKERS");

    private final String value;

    StorageConsumeAction(final String value) {
        this.value = value;
    }

    public static StorageConsumeAction getActionFromString(final String value) {
        return Stream.of(StorageConsumeAction.values())
                .filter(a -> a.getValue().equalsIgnoreCase(value))
                .findAny()
                .orElse(null);
    }

    public static String getStringFromAction(final StorageConsumeAction value) {
        return Optional.ofNullable(value)
                .map(StorageConsumeAction::getValue)
                .orElse(null);
    }
}
