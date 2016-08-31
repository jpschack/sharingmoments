package com.sharingmoments.authserver.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@ComponentScan(basePackages = { "com.sharingmoments.authserver.listener" })
@PropertySource("classpath:email.properties")
public class MailConfig {
	
	@Autowired
    private Environment env;
	
	@Bean
    public JavaMailSenderImpl javaMailSenderImpl() {
        final JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(env.getProperty("mail.host"));
        mailSenderImpl.setPort(env.getProperty("mail.port", Integer.class));
        mailSenderImpl.setProtocol(env.getProperty("mail.protocol"));
        mailSenderImpl.setUsername(env.getProperty("mail.username"));
        mailSenderImpl.setPassword(env.getProperty("mail.password"));
        
        final Properties javaMailProps = new Properties();
        javaMailProps.put("mail.smtp.auth", env.getProperty("mail.smtp.auth"));
        javaMailProps.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable"));
        mailSenderImpl.setJavaMailProperties(javaMailProps);
        
        return mailSenderImpl;
    }
}
