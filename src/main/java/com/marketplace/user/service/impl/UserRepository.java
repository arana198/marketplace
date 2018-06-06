package com.marketplace.user.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.user.domain.UserBO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface UserRepository extends BaseRepository<UserBO, String> {
     Optional<UserBO> findByUsernameAndPassword(String username, String password);

     Optional<UserBO> findByUsername(String username);

     @Query("select u from UserBO u inner join u.roles ur where ur.user.id = u.id and ur.providerUserId = :providerUserId")
     Optional<UserBO> findByProviderUserId(@Param("providerUserId") String providerUserId);
}
