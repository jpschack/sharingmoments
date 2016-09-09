package com.sharingmoments.resource.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import org.springframework.web.multipart.MultipartFile;

import com.sharingmoments.resource.error.ForbiddenException;
import com.sharingmoments.resource.error.ResourceNotFoundException;
import com.sharingmoments.resource.persistence.dto.EventDto;
import com.sharingmoments.resource.persistence.model.Event;
import com.sharingmoments.resource.persistence.model.Location;
import com.sharingmoments.resource.persistence.model.Photo;
import com.sharingmoments.resource.persistence.model.User;
import com.sharingmoments.resource.persistence.service.AwsS3Service;
import com.sharingmoments.resource.persistence.service.EventService;
import com.sharingmoments.resource.persistence.service.LocationService;
import com.sharingmoments.resource.persistence.service.PhotoService;
import com.sharingmoments.resource.persistence.service.UserService;
import com.sharingmoments.resource.security.CurrentUser;


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
    private AwsS3Service awsS3Service;
	
	@Autowired
    private PhotoService photoService;
	
	@Autowired
    private Environment env;
	
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
	
	@RequestMapping(value = {"/{id}"}, method = RequestMethod.PUT)
	public Event modifyEvent(final Locale locale, @PathVariable UUID id, @RequestBody @Valid final EventDto eventDto, @CurrentUser Authentication authentication) {
		Event event = eventService.getEventByID(id);
		final User user = getUserByAuth(authentication);
		
		if (event != null) {
			if (user.equals(event.getUser())) {
				Location location = locationService.getLocationByGoogleLocationID(eventDto.getLocation().getGoogleLocationID());
				if (location == null) {
					location = locationService.saveLocation(new Location(eventDto.getLocation().getGoogleLocationID()));
				}
				
				event.setName(eventDto.getName());
				event.setDescription(eventDto.getDescription());
				event.setStartDate(eventDto.getStartDate());
				event.setEndDate(eventDto.getEndDate());
				event.setMultiDayEvent(eventDto.getMultiDayEvent());
				event.setLocation(location);
				
				return eventService.saveEvent(event);
			} else {
				throw new ForbiddenException();
			}
			
		} else {
			throw new ResourceNotFoundException();
		}
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
	
	@RequestMapping(value = {"/{id}/photo"}, method = RequestMethod.POST)
	public Photo uploadPhoto(final Locale locale, @PathVariable UUID id, @RequestParam("file") MultipartFile file, @CurrentUser Authentication authentication) throws Exception {
		Event event = eventService.getEventByID(id);
		final User user = getUserByAuth(authentication);
		
		if (event != null) {
			try {
				InputStream is = file.getInputStream();
				
				final String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
				final String objectKey = UUID.randomUUID().toString() + fileExtension;
				
				awsS3Service.uploadFile(is, objectKey);
				
				final String url = env.getProperty("aws.region.url") + env.getProperty("aws.bucket.name") + "/" + objectKey;
				
				Photo photo = new Photo();
				photo.setObjectKey(objectKey);
				photo.setUrl(url);
				photo.setUser(user);
				photo.setEvent(event);
				
				return photoService.savePhoto(photo);
			} catch (IOException e) {
				throw new Exception();
			}
		} else {
			throw new ResourceNotFoundException();
		}
	}
	
	@RequestMapping(value = {"/{id}/photo"}, method = RequestMethod.GET)
	public Page<Photo> getPhotos(final Locale locale, @PathVariable UUID id, Pageable pageable, PagedResourcesAssembler<Photo> assembler) {
		Event event = eventService.getEventByID(id);
		
		if (event != null) {
			return photoService.getPhotosByEvent(event, pageable);
		} else {
			throw new ResourceNotFoundException();
		}
	}
	
	@RequestMapping(value = "/{eventId}/photo/{photoId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePhoto(final Locale locale, @PathVariable UUID eventId, @PathVariable UUID photoId, @CurrentUser Authentication authentication) {
		final Event event = eventService.getEventByID(eventId);
		final User user = getUserByAuth(authentication);
		Photo photo = photoService.getPhotoByID(photoId);
		
		if (event != null && photo != null && photo.getEvent().equals(event)) {
			if (photo.getUser().equals(user) || event.getUser().equals(user)) {
				awsS3Service.deleteFile(photo.getObjectKey());
				photoService.deletePhoto(photo);
				return ResponseEntity.ok(null);
			} else {
				throw new ForbiddenException();
			}
		} else {
			throw new ResourceNotFoundException();
		}
	}
	
	private User getUserByAuth(Authentication authentication) {
		User principal = (User) authentication.getPrincipal();
		return userService.getUserByID(principal.getId());
	}
}
