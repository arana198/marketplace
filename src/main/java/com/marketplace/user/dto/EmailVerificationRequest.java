package com.marketplace.user.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class EmailVerificationRequest {

    @NotBlank(message = "email is mandatory")
    @Email(message = "email format incorrect")
    private final String email;

    @NotBlank(message = "token is mandatory")
    private final String token;

    @JsonCreator
    public EmailVerificationRequest(@JsonProperty(value = "email") final String email,
                                    @JsonProperty(value = "token") String token) {
        this.email = email;
        this.token = token;
    }
}
