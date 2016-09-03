package com.sharingmoments.controller;

import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharingmoments.error.ResourceNotFoundException;
import com.sharingmoments.persistence.model.Event;
import com.sharingmoments.persistence.model.Photo;
import com.sharingmoments.persistence.model.User;
import com.sharingmoments.persistence.service.EventService;
import com.sharingmoments.persistence.service.PhotoService;
import com.sharingmoments.persistence.service.UserService;
import com.sharingmoments.security.CurrentUser;


@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private PhotoService photoService;
	
	@Autowired
    private EventService eventService;

	
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	public User getUserData(final Locale locale, @PathVariable("userId") UUID userId) {
		final User user = userService.getUserByID(userId);
		
		if (user != null) {
			return user;
		} else {
			throw new ResourceNotFoundException();
		}
	}
	
	@RequestMapping(value = "/{userId}/photos", method = RequestMethod.GET)
    public Page<Photo> getPhotos(final Locale locale, @PathVariable("userId") UUID userId, Pageable pageable, PagedResourcesAssembler<Photo> assembler) {
		final User user = userService.getUserByID(userId);
		
		if (user == null) {
			throw new ResourceNotFoundException();
		}
		
		return photoService.getPhotosByUser(user, pageable);
	}
	
	@RequestMapping(value = "/{userId}/events", method = RequestMethod.GET)
    public Page<Event> getEvents(final Locale locale, @PathVariable("userId") UUID userId, Pageable pageable, PagedResourcesAssembler<Event> assembler) {
		final User user = userService.getUserByID(userId);
		
		if (user == null) {
			throw new ResourceNotFoundException();
		}
		
		return eventService.getEventsByUser(user, pageable);
    }
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public Page<User> searchForUsers(final Locale locale, @RequestParam("q") String searchString, Pageable pageable, PagedResourcesAssembler<User> assembler, @CurrentUser Authentication authentication) {
		final User user = getUserByAuth(authentication);
		
		return userService.findUserByUsernameOrName(searchString, user.getId(), pageable);
    }
	
	private User getUserByAuth(Authentication authentication) {
		final User principal = (User) authentication.getPrincipal();
		return userService.getUserByID(principal.getId());
	}
}
