package com.marketplace.user.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SocialUserRequest extends UserRequest {

    private final String loginProviderId;
    private final String profileImageUrl;

    @JsonCreator
    public SocialUserRequest(final String email, final String loginProviderId, final String profileImageUrl) {
        super(email, null);
        this.loginProviderId = loginProviderId;
        this.profileImageUrl = profileImageUrl;
    }
}
