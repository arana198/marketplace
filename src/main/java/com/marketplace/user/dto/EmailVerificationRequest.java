package com.marketplace.user.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class EmailVerificationRequest {

    @NotBlank(message = "token is mandatory")
    private final String token;

    @JsonCreator
    public EmailVerificationRequest(@JsonProperty(value = "token") String token) {
        this.token = token;
    }
}
