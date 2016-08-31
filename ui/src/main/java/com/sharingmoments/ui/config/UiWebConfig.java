package com.sharingmoments.ui.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


@Configuration
@EnableWebMvc
public class UiWebConfig extends WebMvcConfigurerAdapter {
	
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");
        registry.addResourceHandler("/pix/**").addResourceLocations("classpath:/static/pix/");
        registry.addResourceHandler("/app/**").addResourceLocations("classpath:/static/app/");
    }
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
       super.addViewControllers(registry);
  
       registry.addViewController("/").setViewName("index/index");;
    }
  
    @Bean
    public ViewResolver viewResolver() {
       InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
  
       viewResolver.setPrefix("/app/");
       viewResolver.setSuffix(".html");
  
       return viewResolver;
    }
}
