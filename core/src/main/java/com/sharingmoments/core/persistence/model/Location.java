package com.sharingmoments.core.persistence.model;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "locations")
public class Location {
	
	@Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
	
	@Column(unique=true)
	private String googleLocationID;
	
	private Date createdAt;
	
	@OneToMany(mappedBy = "location")
    @JsonBackReference
    private Collection<Event> events;
	
	public Location() {
		super();
		this.createdAt = new Date();
	}
	
	public Location(String googleLocationID) {
		super();
		this.googleLocationID = googleLocationID;
		this.createdAt = new Date();
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getGoogleLocationID() {
		return googleLocationID;
	}

	public void setGoogleLocationID(String googleLocationID) {
		this.googleLocationID = googleLocationID;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
