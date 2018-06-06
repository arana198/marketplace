package com.marketplace.storage.queue.publish.domain;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum StoragePublishAction {
     //DOCUMENT
     BUCKET_CREATED("BUCKET_CREATED");


     private final String value;

     StoragePublishAction(final String value) {
          this.value = value;
     }

     public static StoragePublishAction getActionFromString(final String value) {
          return Stream.of(StoragePublishAction.values())
              .filter(a -> a.getValue().equalsIgnoreCase(value))
              .findAny()
              .orElse(null);
     }

     public static String getStringFromAction(final StoragePublishAction value) {
          return Optional.ofNullable(value)
              .map(StoragePublishAction::getValue)
              .orElse(null);
     }
}
