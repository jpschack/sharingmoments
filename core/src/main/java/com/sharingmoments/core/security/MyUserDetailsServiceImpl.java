package com.sharingmoments.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharingmoments.core.error.AccessDeniedException;
import com.sharingmoments.core.persistence.doa.UserRepository;
import com.sharingmoments.core.persistence.model.Privilege;
import com.sharingmoments.core.persistence.model.Role;
import com.sharingmoments.core.persistence.model.User;


@Service("userDetailsService")
@Transactional
public class MyUserDetailsServiceImpl implements MyUserDetailsService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private MessageSource messages;

    public MyUserDetailsServiceImpl() {
        super();
    }
    
    @Override
    //Workaround for now
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
            	user = userRepository.findByEmail(username);
            	
            	 if (user == null) { 
            		 throw new AccessDeniedException(messages.getMessage("message.userNotFound", null, LocaleContextHolder.getLocale()));
            	 }
            }

            return new UserDetailsImpl(user);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public UserDetails loadUserByID(final UUID id) throws UsernameNotFoundException {
        try {
            final User user = userRepository.findOne(id);
            if (user == null) {
                throw new AccessDeniedException("No user found with id: " + id);
            }

            return new UserDetailsImpl(user);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public UserDetails loadUserByEmail(final String email) throws UsernameNotFoundException {
    	try {
            final User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new AccessDeniedException("No user found with email: " + email);
            }

            return new UserDetailsImpl(user);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private final List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<String>();
        final List<Privilege> collection = new ArrayList<Privilege>();
        for (final Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (final Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges) {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (final String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
