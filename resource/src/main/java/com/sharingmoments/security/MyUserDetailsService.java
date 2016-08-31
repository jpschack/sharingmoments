package com.sharingmoments.security;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MyUserDetailsService extends UserDetailsService {
	UserDetails loadUserByID(final UUID id) throws UsernameNotFoundException;
	UserDetails loadUserByEmail(final String email) throws UsernameNotFoundException;
}
