package com.sharingmoments.resource.persistence.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharingmoments.resource.persistence.doa.EventRepository;
import com.sharingmoments.resource.persistence.model.Event;
import com.sharingmoments.resource.persistence.model.User;



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

	@Override
	public Page<Event> findByNameOrDescriptionAndTimeframe(String searchString, Date from, Date to, Pageable pageable) {
		return repository.findByNameOrDescriptionAndTimeframe(searchString, from, to, pageable);
	}

	@Override
	public Page<Event> findByNameOrDescriptionAndFromDate(String searchString, Date from, Pageable pageable) {
		return repository.findByNameOrDescriptionAndFromDate(searchString, from, pageable);
	}

	@Override
	public Page<Event> findByNameOrDescriptionAndToDate(String searchString, Date to, Pageable pageable) {
		return repository.findByNameOrDescriptionAndToDate(searchString, to, pageable);
	}
}
