package com.marketplace.user.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.dto.User;
import org.springframework.stereotype.Service;

@Service
class UserConverter implements BaseConverter<User, UserBO> {

    @Override
    public UserBO convert(final User source) {
        return new UserBO()
                .setUsername(source.getEmail());
    }
}
