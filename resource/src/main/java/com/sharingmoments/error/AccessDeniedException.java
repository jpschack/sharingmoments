package com.sharingmoments.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public final class AccessDeniedException extends RuntimeException {
	private static final long serialVersionUID = -2443400171320149272L;
	
	public AccessDeniedException() {
		super();
	}
	
	public AccessDeniedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AccessDeniedException(final String message) {
        super(message);
    }

    public AccessDeniedException(final Throwable cause) {
        super(cause);
    }
}
