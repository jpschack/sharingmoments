package com.sharingmoments.controller;

import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharingmoments.error.RessourceNotFoundException;
import com.sharingmoments.persistence.dto.EventDto;
import com.sharingmoments.persistence.model.Event;
import com.sharingmoments.persistence.model.Location;
import com.sharingmoments.persistence.model.User;
import com.sharingmoments.persistence.service.EventService;
import com.sharingmoments.persistence.service.LocationService;
import com.sharingmoments.persistence.service.UserService;
import com.sharingmoments.security.CurrentUser;
import com.sharingmoments.util.GenericResponse;

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
			throw new RessourceNotFoundException();
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
			throw new RessourceNotFoundException();
		}
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public GenericResponse searchForEvents(final Locale locale, @RequestParam("q") String searchString, Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		Page<Event> events = eventService.findByNameOrDescription(searchString, pageable);
		
        return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale), assembler.toResource(events));
    }
	
	private User getUserByAuth(Authentication authentication) {
		User principal = (User) authentication.getPrincipal();
		return userService.getUserByID(principal.getId());
	}
}
