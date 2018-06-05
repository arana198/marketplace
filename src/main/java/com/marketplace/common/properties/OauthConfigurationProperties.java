package com.marketplace.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.oauth2")
public class OauthConfigurationProperties {
  private String resourceId;
  private String clientId;
  private String clientSecret;
  private String checkTokenEndpoint;
  private String authorizeEndpoint;
}
