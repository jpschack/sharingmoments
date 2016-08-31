package com.sharingmoments.persistence.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sharingmoments.persistence.model.Event;
import com.sharingmoments.persistence.model.User;

public interface EventService {
	Event getEventByID(UUID id);
	
	Event saveEvent(Event event);
	
	Page<Event> getEventsByUser(User user, Pageable pageable);
	
	Page<Event> findByNameOrDescription(String searchString, Pageable pageable);
	
	void deleteEvent(Event event);
}
