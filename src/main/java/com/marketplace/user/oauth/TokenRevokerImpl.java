package com.marketplace.user.oauth;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Data
@Slf4j
@Service
class TokenRevokerImpl implements TokenRevoker {

    private final JdbcTokenStore jdbcTokenStore;

    @Autowired
    public TokenRevokerImpl(final TokenStore jdbcTokenStore) {
        this.jdbcTokenStore = (JdbcTokenStore) jdbcTokenStore;
    }

    @Override
    public void revoke(final String userId) {
        Collection<OAuth2AccessToken> accessTokenList = jdbcTokenStore.findTokensByUserName(userId);
        accessTokenList.stream().forEach(token -> {
            log.debug("Removing token {} for user {}", token.getValue(), userId);
            jdbcTokenStore.removeAccessToken(token);
            jdbcTokenStore.removeRefreshToken(token.getRefreshToken());
        });
    }
}
