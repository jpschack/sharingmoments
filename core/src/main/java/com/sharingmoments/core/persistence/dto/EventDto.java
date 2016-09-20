package com.sharingmoments.core.persistence.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EventDto {
	
	@NotNull
	@Size(min = 1)
	private String name;
	
	private String description;
	
	@NotNull
	private Date startDate;
	
	@NotNull
	private Date endDate;
	
	@NotNull
	private Boolean multiDayEvent;
	
	@NotNull
	private LocationDto location;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Boolean getMultiDayEvent() {
		return multiDayEvent;
	}

	public void setMultiDayEvent(Boolean multiDayEvent) {
		this.multiDayEvent = multiDayEvent;
	}

	public LocationDto getLocation() {
		return location;
	}

	public void setLocation(LocationDto location) {
		this.location = location;
	}
}
