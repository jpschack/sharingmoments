package com.sharingmoments.core.persistence.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharingmoments.core.persistence.doa.PhotoRepository;
import com.sharingmoments.core.persistence.model.Event;
import com.sharingmoments.core.persistence.model.Photo;
import com.sharingmoments.core.persistence.model.User;



@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {
	@Autowired
    private PhotoRepository repository;

	@Override
	public Photo getPhotoByID(UUID id) {
		return repository.findOne(id);
	}

	@Override
	public Page<Photo> getPhotosByUser(User user, Pageable pageable) {
		return repository.findByUserOrderByCreatedAtDesc(user, pageable);
	}
	
	@Override
	public Page<Photo> getPhotosByEvent(Event event, Pageable pageable) {
		return repository.findByEventOrderByCreatedAtDesc(event, pageable);
	}

	@Override
	public Photo savePhoto(Photo photo) {
		return repository.save(photo);
	}

	@Override
	public void deletePhoto(Photo photo) {
		repository.delete(photo);
	}
}