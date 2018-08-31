package com.marketplace.profile.queue.consume;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum ProfileConsumeAction {
     USER_CREATED("USER_CREATED");

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
