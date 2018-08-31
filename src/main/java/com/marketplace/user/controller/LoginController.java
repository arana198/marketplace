package com.marketplace.user.controller;

import com.marketplace.common.exception.BadRequestException;
import com.marketplace.user.dto.Oauth;
import com.marketplace.user.service.UserConnectionService;
import com.marketplace.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Controller
public class LoginController {

     private static final String SCOPE = "read write";

     private final UserService userService;
     private final TokenEndpoint tokenEndpoint;
     private final ConnectionFactoryLocator connectionFactoryLocator;
     private final ConnectionRepository connectionRepository;
     private final ConnectionSignUp connectionSignUp;
     private final UserConnectionService userConnectionService;
     private final JdbcTokenStore jdbcTokenStore;

     @DeleteMapping(value = "/logout")
     public ResponseEntity<ResourceSupport> logout(final Principal principal) {
          userService.logout(principal);
          return new ResponseEntity<>(HttpStatus.OK);
     }

     @PostMapping(value = "/login")
     public ResponseEntity<OAuth2AccessToken> login(final Principal principal,
                                                    @Valid @RequestBody final Oauth oauth,
                                                    final BindingResult bindingResult) throws HttpRequestMethodNotSupportedException {

          if (bindingResult.hasErrors()) {
               throw new BadRequestException("Invalid oauth object", bindingResult);
          }

          if ("password".equalsIgnoreCase(oauth.getGrantType())
              && !StringUtils.isEmpty(oauth.getUsername())
              && !StringUtils.isEmpty(oauth.getPassword())) {

               final Map<String, String> params = new HashMap<>();
               params.put("grant_type", "password");
               params.put("username", oauth.getUsername());
               params.put("password", oauth.getPassword());
               params.put("scope", SCOPE);

               return tokenEndpoint.postAccessToken(principal, params);
          } else if ("authorize".equalsIgnoreCase(oauth.getGrantType())
              && !StringUtils.isEmpty(oauth.getCode())
              && !StringUtils.isEmpty(oauth.getProviderId())
              && !StringUtils.isEmpty(oauth.getRedirectUrl())) {
               OAuth2ConnectionFactory connectionFactory = (OAuth2ConnectionFactory) connectionFactoryLocator.getConnectionFactory(oauth.getProviderId());
               try {
                    final AccessGrant accessGrant = connectionFactory.getOAuthOperations().exchangeForAccess(oauth.getCode(), oauth.getRedirectUrl(), null);
                    final Connection connection = connectionFactory.createConnection(accessGrant);

                    try {
                         connectionRepository.getConnection(connection.getKey());
                    } catch (NoSuchConnectionException ex) {
                         connectionRepository.addConnection(connection);
                    }

                    final String userId = connectionSignUp.execute(connection);
                    final Map<String, String> params = new HashMap<>();
                    params.put("grant_type", "password");
                    params.put("username", userId);
                    params.put("scope", SCOPE);

                    return tokenEndpoint.postAccessToken(principal, params);
               } catch (HttpClientErrorException var5) {
                    LOGGER.warn("HttpClientErrorException while completing connection: " + var5.getMessage());
                    LOGGER.warn("Response body: " + var5.getResponseBodyAsString());
                    throw new BadRequestException(var5.getResponseBodyAsString());
               }
          } else if ("refresh_token".equalsIgnoreCase(oauth.getGrantType())
              && !StringUtils.isEmpty(oauth.getRefreshToken())) {

               final Map<String, String> params = new HashMap<>();
               params.put("grant_type", oauth.getGrantType());
               params.put("refresh_token", oauth.getRefreshToken());
               params.put("scope", SCOPE);

               final ResponseEntity<OAuth2AccessToken> oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, params);
               if (oAuth2AccessToken.hasBody()) {
                    String username = Optional.ofNullable(oAuth2AccessToken.getBody().getAdditionalInformation())
                        .map(info -> info.get("username"))
                        .map(Object::toString)
                        .orElse(null);

                    if (!userConnectionService.checkValidityForProviderTokenByUser(oauth.getProviderId(), username)) {
                         return oAuth2AccessToken;
                    } else {
                         jdbcTokenStore.removeAccessToken(oAuth2AccessToken.getBody());
                         jdbcTokenStore.removeRefreshToken(oauth.getRefreshToken());
                    }
               }
          }

          throw new BadRequestException("Invalid request");
     }
}
