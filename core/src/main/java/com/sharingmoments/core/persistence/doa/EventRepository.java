package com.sharingmoments.core.persistence.doa;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.sharingmoments.core.persistence.model.Event;
import com.sharingmoments.core.persistence.model.User;


public interface EventRepository extends PagingAndSortingRepository<Event, UUID> {
	
	@Override
    void delete(Event event);
	
	Page<Event> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
	
    @Query("SELECT e FROM Event e where :searchString != '' AND ( e.name LIKE CONCAT('%',:searchString,'%') OR e.description LIKE CONCAT('%',:searchString,'%'))") 
    Page<Event> findByNameOrDescription(@Param("searchString") String searchString, Pageable pageable);
    
    @Query("SELECT e FROM Event e where :searchString != '' AND (e.startDate <= :to AND ((e.multiDayEvent = FALSE AND e.startDate >= :from) OR (e.multiDayEvent = TRUE AND e.endDate >= :from))) AND ( e.name LIKE CONCAT('%',:searchString,'%') OR e.description LIKE CONCAT('%',:searchString,'%'))") 
    Page<Event> findByNameOrDescriptionAndTimeframe(@Param("searchString") String searchString, @Param("from") @Temporal() Date from, @Param("to") @Temporal() Date to, Pageable pageable);
    
    @Query("SELECT e FROM Event e where :searchString != '' AND ((e.startDate >= :from) OR ((e.multiDayEvent = TRUE) AND (e.endDate >= :from))) AND ( e.name LIKE CONCAT('%',:searchString,'%') OR e.description LIKE CONCAT('%',:searchString,'%'))") 
    Page<Event> findByNameOrDescriptionAndFromDate(@Param("searchString") String searchString, @Param("from") @Temporal() Date from, Pageable pageable);
    
    @Query("SELECT e FROM Event e where :searchString != '' AND (e.startDate <= :to) AND ( e.name LIKE CONCAT('%',:searchString,'%') OR e.description LIKE CONCAT('%',:searchString,'%'))") 
    Page<Event> findByNameOrDescriptionAndToDate(@Param("searchString") String searchString, @Param("to") @Temporal() Date to, Pageable pageable);
}