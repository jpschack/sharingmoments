package com.sharingmoments.resource.persistence.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sharingmoments.resource.persistence.model.Event;
import com.sharingmoments.resource.persistence.model.User;


public interface EventService {
	Event getEventByID(UUID id);
	
	Event saveEvent(Event event);
	
	Page<Event> getEventsByUser(User user, Pageable pageable);
	
	Page<Event> findByNameOrDescription(String searchString, Pageable pageable);
	
	void deleteEvent(Event event);
}
