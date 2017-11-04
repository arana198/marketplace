package com.marketplace.profile.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.profile.domain.UserProfileBO;
import com.marketplace.profile.dto.UserProfileResponse;
import org.springframework.stereotype.Service;

@Service
class UserProfileResponseConverter implements BaseConverter<UserProfileBO, UserProfileResponse> {

    @Override
    public UserProfileResponse convert(final UserProfileBO source) {
        return UserProfileResponse.builder()
                .userId(source.getUserId())
                .email(source.getEmail())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .mobileNumber(source.getMobileNumber())
                .postcode(source.getPostcode())
                .build();
    }
}
