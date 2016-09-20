package com.sharingmoments.core.validation;

public class EmailExistsException extends Throwable {
	private static final long serialVersionUID = 2745594829992526768L;
	
	private String messageKey;

	public EmailExistsException(final String message, final String messageKey) {
        super(message);
        this.messageKey = messageKey;
    }

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
}
