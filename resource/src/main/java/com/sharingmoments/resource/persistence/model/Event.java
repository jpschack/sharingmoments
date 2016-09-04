package com.sharingmoments.resource.persistence.model;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "events")
public class Event {
	
	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
	
	private String name;
	
	private String description;
	
	@Temporal(TemporalType.DATE)
	private Date startDate;
	
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	private Boolean multiDayEvent;

	private Date createdAt;

    private Date updatedAt;
    
    @ManyToOne
	@JoinColumn(name="user_id")
    @JsonManagedReference
	private User user;
    
    @ManyToOne
	@JoinColumn(name="location_id")
    @JsonManagedReference
	private Location location;
    
    public Event(){
    	super();
    	this.createdAt = new Date();
    	this.updatedAt = new Date();
    }
    
    public Event(String name, Date startDate, Date endDate, Boolean multiDayEvent, User user, Location location){
    	super();
    	this.name = name;
    	this.user = user;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.multiDayEvent = multiDayEvent;
    	this.location = location;
    	this.createdAt = new Date();
    	this.updatedAt = new Date();
    }
    
    public Event(String name, String description, Date startDate, Date endDate, Boolean multiDayEvent, User user, Location location){
    	super();
    	this.name = name;
    	this.description = description;
    	this.user = user;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.location = location;
    	this.multiDayEvent = multiDayEvent;
    	this.createdAt = new Date();
    	this.updatedAt = new Date();
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Event event = (Event) obj;
        if (!id.equals(event.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Event {id:").append(id).append(",").append("name:").append(name).append(",").append("createdAt:").append(createdAt).append(",").append("updatedAt:").append(updatedAt).append("}");
        return builder.toString();
    }
}
