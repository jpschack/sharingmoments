package com.sharingmoments.core.persistence.service;

import com.sharingmoments.core.persistence.model.Like;

public interface LikeService {
	Like getLikeByID(Long id);
	
	Like saveLike(Like like);
	
	void deleteLike(Like like);
}
