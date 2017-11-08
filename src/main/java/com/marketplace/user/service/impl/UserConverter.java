package com.marketplace.user.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.dto.UserRequest;
import org.springframework.stereotype.Service;

@Service
class UserConverter implements BaseConverter<UserRequest, UserBO> {

    @Override
    public UserBO convert(final UserRequest source) {
        return new UserBO()
                .setUsername(source.getEmail());
    }
}
