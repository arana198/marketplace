package com.marketplace.user.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class UpdatePasswordRequest {

    @Size(min = 6, max = 30, message = "password is wrong size")
    @NotBlank(message = "password is mandatory")
    private final String password;

    @JsonCreator
    public UpdatePasswordRequest(@JsonProperty(value = "password") final String password) {
        this.password = password;
    }
}
