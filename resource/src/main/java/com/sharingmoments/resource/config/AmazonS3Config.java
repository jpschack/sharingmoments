package com.sharingmoments.resource.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

@Configuration
@PropertySource({ "classpath:amazonS3.properties" })
public class AmazonS3Config {
	
	@Autowired
    private Environment env;
	
	@Bean
	public AWSCredentials credential() {
	return new BasicAWSCredentials(env.getProperty("aws.access.key.id"), env.getProperty("aws.secret.access.key"));
	}
	
	@Bean
	public AmazonS3 s3client() {
		return new AmazonS3Client(credential()); 
	}
}
