package com.sharingmoments.persistence.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharingmoments.persistence.model.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);

}
