package com.marketplace.user.oauth;

public interface TokenRevoker {
  void revoke(final String userId);
}
