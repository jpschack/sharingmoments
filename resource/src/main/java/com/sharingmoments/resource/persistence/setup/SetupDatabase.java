package com.sharingmoments.resource.persistence.setup;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sharingmoments.resource.persistence.doa.PrivilegeRepository;
import com.sharingmoments.resource.persistence.doa.RoleRepository;
import com.sharingmoments.resource.persistence.doa.UserRepository;
import com.sharingmoments.resource.persistence.model.Privilege;
import com.sharingmoments.resource.persistence.model.Role;
import com.sharingmoments.resource.persistence.model.User;


@Component
public class SetupDatabase implements ApplicationListener<ContextRefreshedEvent> {
	
	private boolean alreadySetup = false;
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (this.alreadySetup) {
            return;
        }
		
		final Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        final Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        // == create initial roles
        final List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        final Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        createUserIfNotFound("test@test.com", adminRole);

        alreadySetup = true;
	}
	
    private final User createUserIfNotFound(final String email, Role adminRole) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
        	user = new User();

            user.setName("Testname");
            user.setUsername("TestUsername");
            user.setPassword(passwordEncoder.encode("test1234"));
            user.setEmail(email);
            user.setRoles(Arrays.asList(adminRole));
            user.setEnabled(true);
        	
            userRepository.save(user);
        }
        return user;
    }
	
	
    private final Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
