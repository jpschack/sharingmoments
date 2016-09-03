package com.sharingmoments.resource.persistence.doa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.sharingmoments.resource.persistence.model.Event;
import com.sharingmoments.resource.persistence.model.User;


public interface EventRepository extends PagingAndSortingRepository<Event, UUID> {
	
	@Override
    void delete(Event event);
	
	Page<Event> findByUser(User user, Pageable pageable);
	
    @Query("SELECT e FROM Event e where :searchString != '' AND ( e.name LIKE CONCAT('%',:searchString,'%') OR e.description LIKE CONCAT('%',:searchString,'%'))") 
    Page<Event> findByNameOrDescription(@Param("searchString") String searchString, Pageable pageable);
}