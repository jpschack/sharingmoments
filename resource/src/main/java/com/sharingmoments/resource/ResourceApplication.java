package com.sharingmoments.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.aws.context.config.annotation.EnableContextCredentials;
import org.springframework.cloud.aws.context.config.annotation.EnableContextRegion;
import org.springframework.cloud.aws.jdbc.config.annotation.EnableRdsInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableContextCredentials(accessKey="${cloud.aws.credentials.accessKey}", secretKey="${cloud.aws.credentials.secretKey}")
@EnableRdsInstance(databaseName = "${cloud.aws.rds.database.name}", 
                   dbInstanceIdentifier = "${cloud.aws.rds.database.instance.id}", 
                   password = "${cloud.aws.rds.database.password}")
@EnableContextRegion(region = "${cloud.aws.region.static}")
public class ResourceApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}
	
	@Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(11);
    }
}