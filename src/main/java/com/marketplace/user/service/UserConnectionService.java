package com.marketplace.user.service;

public interface UserConnectionService {
     boolean checkValidityForProviderTokenByUser(String providerId, String username);
}
