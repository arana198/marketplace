package com.marketplace.common.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResource {
    private final int code;
    private final String error;
    private final String message;
    private List<FieldErrorResource> fieldErrors;

    public ErrorResource(final int code, final String error, final String message) {
        this.code = code;
        this.error = error;
        this.message = message;
    }
}