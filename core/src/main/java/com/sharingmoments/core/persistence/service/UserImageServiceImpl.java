package com.sharingmoments.core.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharingmoments.core.persistence.doa.UserImageRepository;
import com.sharingmoments.core.persistence.model.User;
import com.sharingmoments.core.persistence.model.UserImage;


@Service
@Transactional
public class UserImageServiceImpl implements UserImageService {
	
	@Autowired
	private UserImageRepository repository;

	@Override
	public UserImage getUserImageByID(long id) {
		return repository.findOne(id);
	}

	@Override
	public UserImage getUserImageByUser(User user) {
		return repository.findByUser(user);
	}

	@Override
	public UserImage saveUserImage(UserImage userImage) {
		return repository.save(userImage);
	}

	@Override
	public void deleteUserImage(UserImage userImage) {
		repository.delete(userImage);
	}
}
