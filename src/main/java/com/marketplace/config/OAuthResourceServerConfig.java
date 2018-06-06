package com.marketplace.config;

import com.marketplace.common.properties.OauthConfigurationProperties;
import com.marketplace.common.security.AuthUser;
import com.marketplace.utils.RolesExtractorUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Configuration
@EnableResourceServer
public class OAuthResourceServerConfig extends ResourceServerConfigurerAdapter {

     private final TokenStore tokenStore;
     private final OauthConfigurationProperties oauthConfigurationProperties;

     @Override
     public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
          resources.resourceId(oauthConfigurationProperties.getResourceId())
              .tokenServices(getTokenServices());
     }

     @Override
     public void configure(final HttpSecurity http) throws Exception {
          // @formatter:off
          http.csrf().disable()
              .logout().disable();
          http
              // For some reason we cant just "permitAll" OPTIONS requests which are needed for CORS support. Spring Security
              // will respond with an HTTP 401 nonetheless.
              // So we just put all other requests types under OAuth control and exclude OPTIONS.
              .authorizeRequests()
              .antMatchers(HttpMethod.OPTIONS).permitAll()
              .antMatchers("/info").permitAll()
              .antMatchers("/metrics").permitAll()
              .antMatchers("/health").permitAll()
              .antMatchers("/webjars/**").permitAll()
              .antMatchers("/connect/**").denyAll()
              .antMatchers("/swagger-ui.js", "/swagger-ui.min.js", "/api-docs", "/fonts/*", "/api-docs/*", "/api-docs/default/*").permitAll()
              .antMatchers(HttpMethod.GET, "/roles").permitAll()
              .antMatchers(HttpMethod.GET, "/services").permitAll()
              .antMatchers(HttpMethod.GET, "/advices").permitAll()
              .antMatchers(HttpMethod.POST, "/users").permitAll()
              .antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('read') or #oauth2.hasScope('all')")
              .antMatchers(HttpMethod.POST, "/**").access("#oauth2.hasScope('write') or #oauth2.hasScope('all')")
              .antMatchers(HttpMethod.PATCH, "/**").access("#oauth2.hasScope('write') or #oauth2.hasScope('all')")
              .antMatchers(HttpMethod.PUT, "/**").access("#oauth2.hasScope('write') or #oauth2.hasScope('all')")
              .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write') or #oauth2.hasScope('all')")
              .anyRequest().authenticated()
              .and()
              // Add headers required for CORS requests.
              .headers().addHeaderWriter((request, response) -> {
               response.addHeader("Access-Control-Allow-Origin", "*");
               if (request.getMethod().equals("OPTIONS")) {
                    response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
                    response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
               }
          });

          // @formatter:on
     }

     @Primary
     @Bean
     CustomTokenServices getTokenServices() {
          return new CustomTokenServices(tokenStore);
     }

     static class CustomTokenServices implements ResourceServerTokenServices {

          private final TokenConverter tokenConverter = new TokenConverter();
          private final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

          CustomTokenServices(final TokenStore tokenStore) {
               defaultTokenServices.setTokenStore(tokenStore);
          }

          @Override
          public OAuth2Authentication loadAuthentication(final String accessToken) throws AuthenticationException, InvalidTokenException {
               OAuth2AccessToken token = this.readAccessToken(accessToken);
               OAuth2Authentication oAuth2Authentication = defaultTokenServices.loadAuthentication(accessToken);
               Map<String, ?> response = tokenConverter.convertAccessToken(token, oAuth2Authentication);
               Assert.state(response.containsKey("client_id"), "Client id must be present in response from auth server");
               return tokenConverter.extractAuthentication(response);
          }

          @Override
          public OAuth2AccessToken readAccessToken(final String accessToken) {
               return defaultTokenServices.readAccessToken(accessToken);
          }
     }

     static class TokenConverter extends DefaultAccessTokenConverter {

          private static final String AUTHORITIES = "authorities";
          private UserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();

          @SuppressWarnings("unchecked")
          @Override
          public OAuth2Authentication extractAuthentication(final Map<String, ?> map) {
               final Map<String, String> parameters = new HashMap<>();
               final Set<String> scope = new LinkedHashSet<>(map.containsKey(SCOPE) ? (Collection<String>) map.get(SCOPE) : Collections.emptySet());
               final Authentication user = userTokenConverter.extractAuthentication(map);

               String clientId = (String) map.get(CLIENT_ID);
               parameters.put(CLIENT_ID, clientId);
               parameters.put(AuthUser.ROLES, RolesExtractorUtils.extract((Map<String, String>) map.get(AuthUser.ROLES)));
               parameters.put(AuthUser.USERNAME, (String) map.get(AuthUser.USERNAME));
               parameters.put(AuthUser.USER_ID, (String) map.get(AuthUser.USER_ID));

               OAuth2Request request = new OAuth2Request(parameters, clientId, new ArrayList((Set) map.get(AUTHORITIES)), true, scope, null, null, null, null);
               return new OAuth2Authentication(request, user);
          }
     }
}