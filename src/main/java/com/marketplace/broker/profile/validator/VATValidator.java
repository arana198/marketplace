package com.marketplace.broker.profile.validator;

public interface VATValidator {
    boolean validate(String companyName, String vatNumber);
}
