package com.marketplace.company.queue.publish.domain;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum CompanyPublishAction {
  //COMPANY
  COMPANY_CREATED("COMPANY_CREATED"),
  COMPANY_UPDATED("COMPANY_UPDATED"),
  COMPANY_INACTIVATED("COMPANY_INACTIVATED"),
  COMPANY_ACTIVATED("COMPANY_ACTIVATED"),
  COMPANY_FCA_NUMBER_VERIFIED("COMPANY_FCA_NUMBER_VERIFIED"),

  //BROKER
  INVITE_BROKER("INVITE_BROKER"),
  BROKER_ADDED_TO_COMPANY("BROKER_ADDED_TO_COMPANY"),
  BROKER_UPDATED_COMPANY("BROKER_ADDED_TO_COMPANY"),
  BROKER_REMOVED_FROM_COMPANY("BROKER_REMOVED_FROM_COMPANY"),
  BROKER_REMOVED_AS_ADMIN("BROKER_REMOVED_AS_ADMIN"),
  BROKER_ADDED_AS_ADMIN("BROKER_ADDED_AS_ADMIN"),
  COMPANY_BROKERS("COMPANY_BROKERS"),
  BROKER_VERIFIED("BROKER_VERIFIED"),
  BROKER_PROFILE_UPDATED("BROKER_PROFILE_UPDATED"),

  //BROKER DOCUMENT
  BROKER_SUBMITTED_CERTIFICATION("BROKER_SUBMITTED_CERTIFICATION"),
  BROKER_CERTIFICATE_VERIFIED("BROKER_CERTIFICATE_VERIFIED");


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
