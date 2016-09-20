package com.sharingmoments.core.validation;

public class UsernameExistsException extends Throwable {
	private static final long serialVersionUID = -6299665066711070219L;
	
	private String messageKey;

	public UsernameExistsException(final String message, final String messageKey) {
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
