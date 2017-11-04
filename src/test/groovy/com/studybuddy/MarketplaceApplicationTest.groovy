package com.marketplace

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.context.WebApplicationContext
import spock.lang.Ignore
import spock.lang.Specification

import javax.sql.DataSource

@Ignore
@WebAppConfiguration
@SpringBootTest(classes = [MarketplaceApplication.class])
class MarketplaceApplicationTest extends Specification {

    @Autowired
    WebApplicationContext context

    @Bean
    DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build()
    }

    def "should boot up without errors"() {
        expect: "web application context exists"
        context != null
    }
}