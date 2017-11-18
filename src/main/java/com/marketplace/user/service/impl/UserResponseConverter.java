package com.marketplace.user.service.impl;

import com.marketplace.common.converter.BaseConverter;
import com.marketplace.user.domain.UserBO;
import com.marketplace.user.dto.UserResponse;
import lombok.Data;
import org.springframework.stereotype.Service;

@Data
@Service
class UserResponseConverter implements BaseConverter<UserBO, UserResponse> {

    private final UserRoleResponseConverter userRoleResponseConverter;

    @Override
    public UserResponse convert(final UserBO source) {
        return new UserResponse(
                source.getId(),
                source.getUsername(),
                userRoleResponseConverter.convert(source.getRoles()),
                source.isEmailVerified());
    }
}
