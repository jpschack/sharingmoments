package com.sharingmoments.resource.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableJpaRepositories(basePackages = "com.sharingmoments.resource.persistence.doa")
@ComponentScan(basePackages = { "com.sharingmoments.resource.persistence.service", "com.sharingmoments.resource.security" })
@EntityScan({"com.sharingmoments.resource.persistence.model"})
@EnableTransactionManagement
public class JPAConfig {}
