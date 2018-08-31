package com.marketplace.user.service;


import com.marketplace.user.domain.UserStatusBO;

import java.util.Optional;

public interface UserStatusService {
     Optional<UserStatusBO> findById(String id);

     Optional<UserStatusBO> findByName(String userStatus);
}
