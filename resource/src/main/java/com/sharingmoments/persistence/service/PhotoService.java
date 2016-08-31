package com.sharingmoments.persistence.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sharingmoments.persistence.model.Photo;
import com.sharingmoments.persistence.model.User;

public interface PhotoService {
	
	Photo getPhotoByID(long id);
	
	Page<Photo> getPhotosByUser(User user, Pageable pageable);
	
	Photo savePhoto(Photo photo);
	
	void deletePhoto(Photo photo);
}