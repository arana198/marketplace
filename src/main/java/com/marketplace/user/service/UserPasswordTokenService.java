package com.marketplace.user.service;

import com.marketplace.user.domain.UserBO;
import com.marketplace.user.domain.UserPasswordTokenBO;

import java.util.Optional;

public interface UserPasswordTokenService {
     void createToken(UserBO userBO);

     void delete(UserPasswordTokenBO userPasswordTokenBO);

     Optional<UserPasswordTokenBO> findByUserIdAndToken(String userId, String token);
}
