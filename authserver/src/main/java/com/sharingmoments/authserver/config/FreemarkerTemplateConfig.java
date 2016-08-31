package com.sharingmoments.authserver.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
public class FreemarkerTemplateConfig {
	
	@Bean
    @ConditionalOnMissingBean(FreeMarkerConfigurer.class)
    public FreeMarkerConfigurer freemarkerConfig() {
        FreeMarkerConfigurer result = new FreeMarkerConfigurer();
        result.setTemplateLoaderPath("classpath:templates");
        
        final Properties properties = new Properties();
        properties.put("auto_import", "spring.ftl as spring");
        result.setFreemarkerSettings(properties);
        
        return result;
    }
	
	@Bean
	public ViewResolver viewResolver() {
		FreeMarkerViewResolver result = new FreeMarkerViewResolver();
		 
		 result.setCache(true);
		 result.setExposeSessionAttributes(true);
		 
		 return result;
	}
}
