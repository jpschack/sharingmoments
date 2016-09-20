package com.sharingmoments.core.persistence.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sharingmoments.core.persistence.model.Event;
import com.sharingmoments.core.persistence.model.Photo;
import com.sharingmoments.core.persistence.model.User;



public interface PhotoService {
	
	Photo getPhotoByID(UUID id);
	
	Page<Photo> getPhotosByUser(User user, Pageable pageable);
	
	Page<Photo> getPhotosByEvent(Event event, Pageable pageable);
	
	Photo savePhoto(Photo photo);
	
	void deletePhoto(Photo photo);
}