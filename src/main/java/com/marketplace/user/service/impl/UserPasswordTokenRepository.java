package com.marketplace.user.service.impl;

import com.marketplace.common.repository.BaseRepository;
import com.marketplace.user.domain.UserPasswordTokenBO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

interface UserPasswordTokenRepository extends BaseRepository<UserPasswordTokenBO, String> {

    @Query("select upt from UserPasswordTokenBO upt where upt.user.id = :userId and upt.token = :token")
    Optional<UserPasswordTokenBO> findByUserIdAndToken(@Param("userId") String userId, @Param("token") String token);

    @Query("select upt from UserPasswordTokenBO upt where upt.user.id = :userId")
    Optional<UserPasswordTokenBO> findByUserId(@Param("userId") String userId);
}
