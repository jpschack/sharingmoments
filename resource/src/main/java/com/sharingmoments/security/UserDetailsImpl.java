package com.sharingmoments.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.sharingmoments.persistence.model.User;

public class UserDetailsImpl extends User implements UserDetails {
	
	private static final long serialVersionUID = -6385096875680365710L;

	public UserDetailsImpl(User user) {
        super(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        /*
        for (final Role role : super.getRoles()) {
        	authorityList = AuthorityUtils.createAuthorityList(role.getName());
        }
        */
        return authorityList;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
    	return true;
    }
}
