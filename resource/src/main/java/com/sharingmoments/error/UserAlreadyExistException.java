package com.sharingmoments.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public final class UserAlreadyExistException extends RuntimeException {

    private static final long serialVersionUID = 5861310537366287163L;
    
    private String messageKey;

    public UserAlreadyExistException() {
        super();
    }

    public UserAlreadyExistException(final String message, final String messageKey, final Throwable cause) {
        super(message, cause);
        this.messageKey = messageKey;
    }

    public UserAlreadyExistException(final String message, final String messageKey) {
        super(message);
        this.messageKey = messageKey;
    }

    public UserAlreadyExistException(final Throwable cause) {
        super(cause);
    }

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
}
