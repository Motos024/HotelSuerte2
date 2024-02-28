package io.bootify.hotel_benidorm.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.bootify.hotel_benidorm.domain")
@EnableJpaRepositories("io.bootify.hotel_benidorm.repos")
@EnableTransactionManagement
public class DomainConfig {
}
