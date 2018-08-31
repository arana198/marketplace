package com.marketplace;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class MarketplaceApplication {

     public static void main(final String[] args) {
          SpringApplication.run(MarketplaceApplication.class, args);
     }

     @Bean
     public Gson gson() {
          return new Gson();
     }
}
