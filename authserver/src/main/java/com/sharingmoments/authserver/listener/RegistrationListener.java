package com.sharingmoments.authserver.listener;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import com.sharingmoments.authserver.util.EmailSender;
import com.sharingmoments.core.persistence.model.User;
import com.sharingmoments.core.persistence.service.UserService;

@Component
public class RegistrationListener implements ApplicationListener<RegistrationCompleteEvent> {
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private EmailSender htmlMailSender;
	
	@Autowired
    private MessageSource messages;
	
	static Logger logger = Logger.getLogger(RegistrationListener.class.getName());

	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		final User user = event.getUser();
        final String token = UUID.randomUUID().toString();
        userService.createVerificationTokenForUser(user, token);

        constructEmailMessage(event, user, token);
	}
	
	private final Boolean constructEmailMessage(final RegistrationCompleteEvent event, final User user, final String token) {
        final String to = user.getEmail();
        final String subject = messages.getMessage("email.signup.subject", null, event.getLocale());
        final String confirmationUrl = event.getAppUrl() + "/account/registrationConfirm?token=" + token;
        
        final ModelMap model = new ModelMap();
        model.addAttribute("headline", messages.getMessage("email.signup.headline", null, event.getLocale()));
        
        String address = messages.getMessage("email.address", null, event.getLocale());
        if(user.getName() == null || user.getName().equals("")) {
        	address += " @" + user.getUsername() + ",";
        } else {
        	address += " " + user.getName() + ",";
        }
        
        model.addAttribute("address", address);
        model.addAttribute("intro", messages.getMessage("email.signup.intro", null, event.getLocale()));
        model.addAttribute("linkText", messages.getMessage("email.signup.link.text", null, event.getLocale()));
        model.addAttribute("welcome", messages.getMessage("email.signup.welcome", null, event.getLocale()));
        model.addAttribute("greetings", messages.getMessage("email.greetings", null, event.getLocale()));
        model.addAttribute("confirmationUrl", confirmationUrl);

        return htmlMailSender.sendAsHTML(to, subject, model, "confirmRegistrationMail.ftl");
    }
}
