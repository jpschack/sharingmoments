package com.sharingmoments.resource.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharingmoments.resource.persistence.doa.LikeRepository;
import com.sharingmoments.resource.persistence.model.Like;


@Service
@Transactional
public class LikeServiceImpl implements LikeService {
	
	@Autowired
	private LikeRepository repository;

	@Override
	public Like getLikeByID(Long id) {
		return repository.findOne(id);
	}

	@Override
	public Like saveLike(Like like) {
		return repository.save(like);
	}

	@Override
	public void deleteLike(Like like) {
		repository.delete(like);
	}
}
