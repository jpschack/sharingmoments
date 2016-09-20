package com.sharingmoments.core.security;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenHandler {
	private final String secret;
	
    private MyUserDetailsService userDetailsService;
    
    static Logger logger = Logger.getLogger(TokenHandler.class.getName());

    @Autowired
    public TokenHandler(@Value("${token.secret}") String secret, MyUserDetailsService userDetailsService) {
        this.secret = secret;
        this.userDetailsService = userDetailsService;
    }

    public UserDetails parseUserFromToken(String token) {
        String uuidString = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        UUID userID = UUID.fromString(uuidString);
        return userDetailsService.loadUserByID(userID);
    }

    public String createTokenForUser(UserDetailsImpl user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
