package com.marketplace.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Data
@Configuration
@EnableResourceServer
public class OAuthResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final Environment env;

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(env.getProperty("oauth.resourceId"));
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
        http.csrf().disable();
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
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
                .antMatchers(HttpMethod.GET, "/me").authenticated()
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
}