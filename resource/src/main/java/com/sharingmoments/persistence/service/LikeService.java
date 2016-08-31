package com.sharingmoments.persistence.service;

import com.sharingmoments.persistence.model.Like;

public interface LikeService {
	Like getLikeByID(Long id);
	
	Like saveLike(Like like);
	
	void deleteLike(Like like);
}
