package com.sharingmoments.resource.persistence.doa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.sharingmoments.resource.persistence.model.User;


public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    User findByEmail(String email);
    
    User findByUsername(String username);
    
    @Query("SELECT u FROM User u where :searchString != '' AND u.id != :currentUserId AND ( u.username LIKE CONCAT('%',:searchString,'%') OR u.name LIKE CONCAT('%',:searchString,'%'))") 
    Page<User> findByUsernameOrName(@Param("searchString") String searchString, @Param("currentUserId") UUID currentUserId, Pageable pageable);

    @Override
    void delete(User user);
}
