package com.sharingmoments.ui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	public void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http
			.antMatcher("/**").authorizeRequests()
			.antMatchers(HttpMethod.GET, "/", "/login", "/update-password", "/reset-password", "/signup", "/account/**", "/profile", "/search*", "/user/**", "/event/**", "/app/**", "/fonts/**", "/pix/**").permitAll()
			.antMatchers("/api/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.csrf().disable();
		//@formatter:on
	}
}
