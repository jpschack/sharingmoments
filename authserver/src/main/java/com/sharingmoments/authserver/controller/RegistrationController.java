package com.sharingmoments.authserver.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.sharingmoments.authserver.listener.RegistrationCompleteEvent;
import com.sharingmoments.authserver.util.EmailSender;
import com.sharingmoments.core.error.UserAlreadyExistException;
import com.sharingmoments.core.error.UserNotFoundException;
import com.sharingmoments.core.persistence.dto.UserDto;
import com.sharingmoments.core.persistence.model.PasswordResetToken;
import com.sharingmoments.core.persistence.model.User;
import com.sharingmoments.core.persistence.model.VerificationToken;
import com.sharingmoments.core.persistence.service.UserService;
import com.sharingmoments.core.security.TokenAuthenticationService;
import com.sharingmoments.core.security.UserAuthentication;
import com.sharingmoments.core.util.JsonArg;
import com.sharingmoments.core.util.JsonPathArgumentResolver;
import com.sharingmoments.core.validation.EmailExistsException;
import com.sharingmoments.core.validation.UsernameExistsException;

@Controller
@RequestMapping(value = "/account")
public class RegistrationController extends WebMvcConfigurerAdapter {

	@Autowired
    private UserService userService;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;
	
	@Autowired
    private ApplicationEventPublisher eventPublisher;
	
	@Autowired
    private MessageSource messages;
	
	@Autowired
    private Environment env;
	
	@Autowired
    private EmailSender htmlMailSender;
	
	
    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody User registerUserAccount(@RequestBody @Valid final UserDto accountDto, final HttpServletRequest request, HttpServletResponse response) {
        User user;
        try {
        	user = createUserAccount(accountDto);
		} catch (EmailExistsException e) {
			throw new UserAlreadyExistException(e.getMessage(), e.getMessageKey(), e);
		} catch (UsernameExistsException e) {
			throw new UserAlreadyExistException(e.getMessage(), e.getMessageKey(), e);
		}

        String serverPort = "";
        if (request.getServerPort() != 80) {
            serverPort = ":" + request.getServerPort();
        }
        
        final String appUrl = "http://" + request.getServerName() + serverPort + request.getContextPath();
        eventPublisher.publishEvent(new RegistrationCompleteEvent(user, request.getLocale(), appUrl));
        
        final UserDetails authenticatedUser = userDetailsService.loadUserByUsername(user.getEmail());
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        tokenAuthenticationService.addAuthentication(response, userAuthentication);

        return user;
    }
    
    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final Locale locale, @RequestParam("token") final String token) {
        final VerificationToken verificationToken = userService.getVerificationToken(token);
        
        if (verificationToken == null) {
            return "redirect:" + env.getProperty("sm.ui.baseUrl");
        }

        final User user = verificationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return "redirect:" + env.getProperty("sm.ui.baseUrl");
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        
        return "redirect:" + env.getProperty("sm.ui.baseUrl") + "/?registrationConfirm=succeeded";
    }
    
    @RequestMapping(value = "/resendRegistrationToken", method = RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    public @ResponseBody ResponseEntity<?> resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        String serverPort = "";
        if (request.getServerPort() != 80) {
        	serverPort = ":" + request.getServerPort();
        }
        
        final String appUrl = "http://" + request.getServerName() + serverPort + request.getContextPath();
        
        final String to = user.getEmail();
        final String subject = messages.getMessage("email.signup.subject", null, request.getLocale());
        final String confirmationUrl = appUrl + "/account/registrationConfirm?token=" + newToken;
        
        String address = messages.getMessage("email.address", null, request.getLocale());
        if(user.getName() == null || user.getName().equals("")) {
        	address += " @" + user.getUsername() + ",";
        } else {
        	address += " " + user.getName() + ",";
        }
        
        final ModelMap model = new ModelMap();
        model.addAttribute("headline", messages.getMessage("email.signup.headline", null, request.getLocale()));
        
        model.addAttribute("address", address);
        model.addAttribute("intro", messages.getMessage("email.signup.intro", null, request.getLocale()));
        model.addAttribute("linkText", messages.getMessage("email.signup.link.text", null, request.getLocale()));
        model.addAttribute("welcome", messages.getMessage("email.signup.welcome", null, request.getLocale()));
        model.addAttribute("greetings", messages.getMessage("email.greetings", null, request.getLocale()));
        model.addAttribute("confirmationUrl", confirmationUrl);
        
        htmlMailSender.sendAsHTML(to, subject, model, "confirmRegistrationMail.ftl");

        return ResponseEntity.ok(null);
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    @ResponseStatus(value=HttpStatus.OK)
    public @ResponseBody ResponseEntity<?> resetPassword(final HttpServletRequest request, @JsonArg final String email) {
        final User user = userService.findUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }

        String serverPort = "";
        if (request.getServerPort() != 80) {
            serverPort = ":" + request.getServerPort();
        }

        final String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        
        final String to = user.getEmail();
        final String subject = messages.getMessage("email.reset.password.subject", null, request.getLocale());
        final String appUrl = "http://" + request.getServerName() + serverPort + request.getContextPath();
        final String resetUrl = appUrl + "/account/changePassword?id=" + user.getId() + "&token=" + token;
        
        String address = messages.getMessage("email.address", null, request.getLocale());
        if(user.getName() == null || user.getName().equals("")) {
        	address += " @" + user.getUsername() + ",";
        } else {
        	address += " " + user.getName() + ",";
        }
        
        final ModelMap model = new ModelMap();
        model.addAttribute("address", address);
        model.addAttribute("intro", messages.getMessage("email.reset.password.intro", null, request.getLocale()));
        model.addAttribute("resetUrl", resetUrl);
        model.addAttribute("linkText", messages.getMessage("email.reset.password.link.text", null, request.getLocale()));
        model.addAttribute("info", messages.getMessage("email.reset.password.info", null, request.getLocale()));
        
        htmlMailSender.sendAsHTML(to, subject, model, "resetPasswordMail.ftl");
        
        return ResponseEntity.ok(null);
    }
    
    @RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String showChangePasswordPage(final Locale locale, @RequestParam("id") final UUID id, @RequestParam("token") final String token) {
    	
        final User user = getUserByPasswordResetToken(token, id);
        if (user == null) {
            return "redirect:" + env.getProperty("sm.ui.baseUrl") + "/?redirectUrl=updatePassword&status=badtoken";
        }
        
        return "redirect:" + env.getProperty("sm.ui.baseUrl") + "/?redirectUrl=updatePassword&status=succeeded&id=" + id + "&token=" + token;
    }
    
    @RequestMapping(value = "/savePassword", method = RequestMethod.POST)
    @ResponseStatus(value=HttpStatus.OK)
    public @ResponseBody User savePassword(final Locale locale, @JsonArg final String password, @JsonArg final UUID id, @JsonArg final String token, HttpServletResponse response) {
    	
    	final User user = getUserByPasswordResetToken(token, id);
        if (user == null) {
        	throw new UserNotFoundException();
        }
        
        userService.changeUserPassword(user, password);
        
        final UserDetails authenticatedUser = userDetailsService.loadUserByUsername(user.getEmail());
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);
        tokenAuthenticationService.addAuthentication(response, userAuthentication);
        
        return user;
    }
    
    private User createUserAccount(final UserDto accountDto) throws EmailExistsException, UsernameExistsException {
        return userService.registerNewUserAccount(accountDto);
    }
    
    private User getUserByPasswordResetToken(String token, UUID userID) {
    	final PasswordResetToken passToken = userService.getPasswordResetToken(token);
    	final User user = passToken.getUser();
    	if ((passToken == null) || (!user.getId().equals(userID))) {
            return null;
        }
    	
    	final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return null;
        }
        return user;
    }
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new JsonPathArgumentResolver());
    }
}