package com.sharingmoments.resource.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class ResourceIsLockedException extends RuntimeException {
	private static final long serialVersionUID = -3611126033832396913L;
}