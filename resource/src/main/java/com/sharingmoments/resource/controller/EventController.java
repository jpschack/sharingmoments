package com.sharingmoments.resource.controller;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharingmoments.resource.error.ResourceNotFoundException;
import com.sharingmoments.resource.persistence.dto.EventDto;
import com.sharingmoments.resource.persistence.model.Event;
import com.sharingmoments.resource.persistence.model.Location;
import com.sharingmoments.resource.persistence.model.User;
import com.sharingmoments.resource.persistence.service.EventService;
import com.sharingmoments.resource.persistence.service.LocationService;
import com.sharingmoments.resource.persistence.service.UserService;
import com.sharingmoments.resource.security.CurrentUser;
import com.sharingmoments.resource.util.GenericResponse;


@RestController
@RequestMapping("/event")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private LocationService locationService;
	
	@Autowired
    private MessageSource messages;
	
	@RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
	public Event createEvent(final Locale locale, @RequestBody @Valid final EventDto eventDto, @CurrentUser Authentication authentication) {
		final User user = getUserByAuth(authentication);
		
		Location location = locationService.getLocationByGoogleLocationID(eventDto.getLocation().getGoogleLocationID());
		if (location == null) {
			location = locationService.saveLocation(new Location(eventDto.getLocation().getGoogleLocationID()));
		}
				
		Event event;
		if (eventDto.getDescription() != null) {
			event = new Event(eventDto.getName(), eventDto.getDescription(), eventDto.getStartDate(), eventDto.getEndDate(), eventDto.getMultiDayEvent(), user, location);
		} else {
			event = new Event(eventDto.getName(), eventDto.getStartDate(), eventDto.getEndDate(), eventDto.getMultiDayEvent(), user, location);
		}
		
		return eventService.saveEvent(event);
	}
	
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.GET)
	public Event getEvent(final Locale locale, @PathVariable UUID id) {
		Event event = eventService.getEventByID(id);
		if (event != null) {
			return event;
		} else {
			throw new ResourceNotFoundException();
		}
	}
	
	@RequestMapping(value = {"/", ""}, method = RequestMethod.PUT)
	public GenericResponse modifyEvent(final Locale locale) {
		return new GenericResponse(messages.getMessage("message.event.modified.success", null, locale), null);
	}
	
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteEvent(final Locale locale, @PathVariable UUID id) {
		Event event = eventService.getEventByID(id);
		
		if (event != null) {
			eventService.deleteEvent(event);
			return ResponseEntity.ok(null);
		} else {
			throw new ResourceNotFoundException();
		}
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
    public Page<Event> searchForEventsWithDateFilter(final Locale locale, @RequestParam("q") String searchString, @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date from, @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date to, Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		if (from != null && to == null) {
			return eventService.findByNameOrDescriptionAndFromDate(searchString, from, pageable);
		} else if (from == null && to != null) {
			return eventService.findByNameOrDescriptionAndToDate(searchString, to, pageable);
		} else if (from != null && to != null) {
			return eventService.findByNameOrDescriptionAndTimeframe(searchString, from, to, pageable);
		} else {
			return eventService.findByNameOrDescription(searchString, pageable);
		}
    }
	
	private User getUserByAuth(Authentication authentication) {
		User principal = (User) authentication.getPrincipal();
		return userService.getUserByID(principal.getId());
	}
}
