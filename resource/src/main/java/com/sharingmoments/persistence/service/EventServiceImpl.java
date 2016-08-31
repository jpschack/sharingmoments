package com.sharingmoments.persistence.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharingmoments.persistence.doa.EventRepository;
import com.sharingmoments.persistence.model.Event;
import com.sharingmoments.persistence.model.User;

@Service
@Transactional
public class EventServiceImpl implements EventService {
	
	@Autowired
	private EventRepository repository;

	@Override
	public Event getEventByID(UUID id) {
		return repository.findOne(id);
	}

	@Override
	public Event saveEvent(Event event) {
		return repository.save(event);
	}

	@Override
	public void deleteEvent(Event event) {
		repository.delete(event);
	}

	@Override
	public Page<Event> getEventsByUser(User user, Pageable pageable) {
		return repository.findByUser(user, pageable);
	}
	
	@Override
	public Page<Event> findByNameOrDescription(String searchString, Pageable pageable) {
		return repository.findByNameOrDescription(searchString, pageable);
	}
}
