package com.sharingmoments.core.persistence.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharingmoments.core.persistence.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}
