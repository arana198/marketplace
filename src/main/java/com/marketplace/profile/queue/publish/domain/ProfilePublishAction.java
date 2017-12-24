package com.marketplace.profile.queue.publish.domain;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum ProfilePublishAction {
    USER_PROFILE_CREATED("USER_PROFILE_CREATED"),
    USER_PROFILE_UPDATED("USER_PROFILE_UPDATED");

    private final String value;

    ProfilePublishAction(final String value) {
        this.value = value;
    }

    public static ProfilePublishAction getActionFromString(final String value) {
        return Stream.of(ProfilePublishAction.values())
                .filter(a -> a.getValue().equalsIgnoreCase(value))
                .findAny()
                .orElse(null);
    }

    public static String getStringFromAction(final ProfilePublishAction value) {
        return Optional.ofNullable(value)
                .map(ProfilePublishAction::getValue)
                .orElse(null);
    }
}
