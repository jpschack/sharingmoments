package com.sharingmoments.resource.persistence.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sharingmoments.resource.persistence.model.Event;
import com.sharingmoments.resource.persistence.model.Photo;
import com.sharingmoments.resource.persistence.model.User;



public interface PhotoService {
	
	Photo getPhotoByID(UUID id);
	
	Page<Photo> getPhotosByUser(User user, Pageable pageable);
	
	Page<Photo> getPhotosByEvent(Event event, Pageable pageable);
	
	Photo savePhoto(Photo photo);
	
	void deletePhoto(Photo photo);
}