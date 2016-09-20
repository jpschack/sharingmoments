package com.sharingmoments.authserver.listener;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.sharingmoments.core.persistence.model.User;

public class RegistrationCompleteEvent extends ApplicationEvent {

	private static final long serialVersionUID = 5277278060938243684L;
	private final User user;
	private final String appUrl;
    private final Locale locale;
	
	public RegistrationCompleteEvent(User user, final Locale locale, final String appUrl) {
		super(user);
		this.user = user;
		this.locale = locale;
		this.appUrl = appUrl;
	}

	public User getUser() {
		return user;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public Locale getLocale() {
		return locale;
	}
}
