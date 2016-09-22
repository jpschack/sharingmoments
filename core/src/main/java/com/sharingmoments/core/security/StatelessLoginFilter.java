package com.sharingmoments.core.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharingmoments.core.persistence.model.User;
import com.sharingmoments.core.security.MyUserDetailsService;
import com.sharingmoments.core.security.TokenAuthenticationService;
import com.sharingmoments.core.security.UserAuthentication;
import com.sharingmoments.core.security.UserDetailsImpl;
import com.sharingmoments.core.util.UserLoginMapper;


public class StatelessLoginFilter extends AbstractAuthenticationProcessingFilter {
	
	private final TokenAuthenticationService tokenAuthenticationService;
    private final MyUserDetailsService userDetailsService;
	    
	public StatelessLoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService, MyUserDetailsService userDetailsService, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(urlMapping));
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userDetailsService = userDetailsService;
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        final UserLoginMapper user = new ObjectMapper().readValue(request.getInputStream(), UserLoginMapper.class);
        final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        
        return getAuthenticationManager().authenticate(loginToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final UserDetails authenticatedUser;
        
        if (authResult.getPrincipal() instanceof User) {
            authenticatedUser = new UserDetailsImpl((User) authResult.getPrincipal());
        } else {
            authenticatedUser = userDetailsService.loadUserByUsername(authResult.getName());
        }

        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        // Add the custom token as HTTP header to the response
        tokenAuthenticationService.addAuthentication(response, userAuthentication);
        
        //Add UserDetails to the response
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String json = mapper.writeValueAsString(authenticatedUser);
        response.getWriter().write(json);

        // Add the authentication to the Security context
        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }
}
