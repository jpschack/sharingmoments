package com.sharingmoments.resource.persistence.dto;

import javax.validation.constraints.NotNull;

public class LocationDto {
	
	@NotNull
	private String googleLocationID;

	public String getGoogleLocationID() {
		return googleLocationID;
	}

	public void setGoogleLocationID(String googleLocationID) {
		this.googleLocationID = googleLocationID;
	}
}