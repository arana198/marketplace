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
public class ForgottenPasswordRequest {

    @NotBlank(message = "email is mandatory")
    @Email(message = "email format incorrect")
    private final String email;

    @Size(min = 6, max = 30, message = "password is wrong size")
    @NotBlank(message = "password is mandatory")
    private final String password;

    @NotBlank(message = "password is mandatory")
    private final String token;

    @JsonCreator
    public ForgottenPasswordRequest(@JsonProperty(value = "email") final String email,
                                    @JsonProperty(value = "password") final String password,
                                    @JsonProperty(value = "token") String token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }
}
