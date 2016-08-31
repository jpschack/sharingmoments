package com.sharingmoments.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharingmoments.persistence.doa.PhotoRepository;
import com.sharingmoments.persistence.model.Photo;
import com.sharingmoments.persistence.model.User;

@Service
@Transactional
public class PhotoServiceImpl implements PhotoService {
	@Autowired
    private PhotoRepository repository;

	@Override
	public Photo getPhotoByID(long id) {
		return repository.findOne(id);
	}

	@Override
	public Page<Photo> getPhotosByUser(User user, Pageable pageable) {
		return repository.findByUser(user, pageable);
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