package com.marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
@IntegrationComponentScan
@EnableTransactionManagement
@EnableJpaAuditing
@EnableJpaRepositories
class JpaRepositoryConfig {

  @Primary
  @Bean
  public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory,
                                                          final DataSource dataSource) {
    JpaTransactionManager tm = new JpaTransactionManager();
    tm.setEntityManagerFactory(entityManagerFactory);
    tm.setDataSource(dataSource);
    return tm;
  }
}