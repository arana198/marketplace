package com.marketplace.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

     @Autowired
     private AuthenticationManager authenticationManager;

     @Override
     protected void configure(final HttpSecurity http) throws Exception {
          // @formatter:off
          http.csrf().disable()
              .logout().disable()
              .requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
              .and()
              .authorizeRequests().anyRequest().authenticated();
          // @formatter:on
     }

     @Override
     protected AuthenticationManager authenticationManager() throws Exception {
          return authenticationManager;
     }
}