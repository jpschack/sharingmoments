package com.sharingmoments.resource.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sharingmoments.resource.error.ForbiddenException;
import com.sharingmoments.resource.persistence.dto.UserUpdateDto;
import com.sharingmoments.resource.persistence.model.User;
import com.sharingmoments.resource.persistence.service.UserService;
import com.sharingmoments.resource.security.CurrentUser;
import com.sharingmoments.resource.util.GenericResponse;
import com.sharingmoments.resource.util.JsonArg;



@RestController
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private MessageSource messages;
		
	@RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
	public User getUser(final Locale locale, @CurrentUser Authentication authentication) {
		final User user = getUserByAuth(authentication);
		return user;
	}
	
	@RequestMapping(value = {"/", ""}, method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(final Locale locale, @CurrentUser Authentication authentication) {
		final User user = getUserByAuth(authentication);
		
        userService.deleteUser(user);
        
        return ResponseEntity.ok(null);
    }
	
	@RequestMapping(value = {"/", ""}, method = RequestMethod.PUT)
    public GenericResponse modify(final Locale locale, @CurrentUser Authentication authentication, @RequestBody @Valid final UserUpdateDto accountDto, HttpServletResponse response) {
		final User user = getUserByAuth(authentication);
		
		List<FieldError> errors = new ArrayList<FieldError>();
        
        if (!user.getEmail().equals(accountDto.getEmail())) {
        	if (userService.emailExists(accountDto.getEmail())) {
        		errors.add(new FieldError("user", "email", messages.getMessage("message.user.modify.error.email", null, locale)));
        	} else {
        		user.setEmail(accountDto.getEmail());
        	}
        }
        
        if (!user.getUsername().equals(accountDto.getUsername())) {
        	if (userService.usernameExists(accountDto.getUsername())) {
        		errors.add(new FieldError("user", "username", messages.getMessage("message.user.modify.error.username", null, locale)));
        	} else {
        		user.setUsername(accountDto.getUsername());
        	}
        }
        
        if (accountDto.getName() != null && user.getName().equals(accountDto.getName())) {
        	user.setName(accountDto.getName());
        }
        
        if (errors.isEmpty()) {
        	userService.saveRegisteredUser(user);
            return new GenericResponse(messages.getMessage("message.user.modify.success", null, locale), user);
        } else {
        	response.setStatus(403);
        	return new GenericResponse(messages.getMessage("message.user.modify.error", null, locale), messages.getMessage("message.user.modify.error", null, locale), errors);
        }
    }
	
	@RequestMapping(value = "/password", method = RequestMethod.PUT)
    public ResponseEntity<?> savePassword(final Locale locale, @CurrentUser Authentication authentication, @JsonArg final String oldPassword, @JsonArg final String password) {
		final User user = getUserByAuth(authentication);
		
        if (userService.checkIfValidOldPassword(user, oldPassword)) {
        	userService.changeUserPassword(user, password);
        	return ResponseEntity.ok(null);
    	} else {
        	throw new ForbiddenException();
        }
    }
	
	@RequestMapping(value = "/privacy", method = RequestMethod.PUT)
    public User changePrivacy(final Locale locale, @CurrentUser Authentication authentication, @JsonArg final Boolean privateAccount) {
		final User user = getUserByAuth(authentication);
		
		user.setPrivateAccount(privateAccount);
        userService.saveRegisteredUser(user);
        
        return user;
    }
	
	private User getUserByAuth(Authentication authentication) {
		final User principal = (User) authentication.getPrincipal();
		return userService.getUserByID(principal.getId());
	}
}
