package com.marketplace.user.service;

import com.marketplace.user.domain.UserPasswordTokenBO;

import java.util.Optional;

public interface UserPasswordTokenService {
    void save(UserPasswordTokenBO userPasswordTokenBO);

    void delete(UserPasswordTokenBO userPasswordTokenBO);

    Optional<UserPasswordTokenBO> findByUserIdAndToken(String userId, String token);

    UserPasswordTokenBO findByUserId(String userId);
}
