package com.marketplace.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.google.connect.GoogleConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;

import javax.sql.DataSource;

@AllArgsConstructor
@EnableSocial
@Configuration
public class SocialConfig implements SocialConfigurer {

  private final DataSource dataSource;
  private final ConnectionSignUp connectionSignUp;

  @Override
  public UsersConnectionRepository getUsersConnectionRepository(final ConnectionFactoryLocator connectionFactoryLocator) {
    JdbcUsersConnectionRepository jdbcUsersConnectionRepository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
    jdbcUsersConnectionRepository.setConnectionSignUp(connectionSignUp);
    return jdbcUsersConnectionRepository;
  }

  @Override
  public void addConnectionFactories(final ConnectionFactoryConfigurer connectionFactoryConfigurer, final Environment environment) {
    connectionFactoryConfigurer.addConnectionFactory(new GoogleConnectionFactory(
        environment.getProperty("spring.social.google.appId"),
        environment.getProperty("spring.social.google.appSecret")));
  }

  @Override
  public UserIdSource getUserIdSource() {
    return new AuthenticationNameUserIdSource();
  }
}
