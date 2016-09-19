package com.sharingmoments.authserver.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sharingmoments.resource.persistence.service.AwsS3ServiceImpl;

@Configuration
@EnableJpaRepositories(basePackages = "com.sharingmoments.resource.persistence.doa")
@ComponentScan(basePackages = { "com.sharingmoments.resource.persistence.service", "com.sharingmoments.resource.security" }, excludeFilters = @ComponentScan.Filter(value = AwsS3ServiceImpl.class, type = FilterType.ASSIGNABLE_TYPE))
@EntityScan({"com.sharingmoments.resource.persistence.model"})
@EnableTransactionManagement
public class JPAConfig {}
