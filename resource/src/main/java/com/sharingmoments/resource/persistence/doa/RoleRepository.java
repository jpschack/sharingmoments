package com.sharingmoments.resource.persistence.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharingmoments.resource.persistence.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}
