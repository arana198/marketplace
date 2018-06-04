package com.marketplace;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@IntegrationComponentScan
public class MarketplaceApplication {

  public static void main(final String[] args) {
    SpringApplication.run(MarketplaceApplication.class, args);
  }

  @Bean
  public Gson gson() {
    return new Gson();
  }
}
