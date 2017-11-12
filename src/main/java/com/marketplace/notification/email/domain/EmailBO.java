package com.marketplace.notification.email.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
public class EmailBO {

    public enum Status {
        SUCCESS, FAILED;
    }

    private String fromAddress;
    private String toAddress;
    private String subject;
    private String content;
    private Status status;
    private String errorMessage;
}
