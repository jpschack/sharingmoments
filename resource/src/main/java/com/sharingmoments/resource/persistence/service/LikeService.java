package com.sharingmoments.resource.persistence.service;

import com.sharingmoments.resource.persistence.model.Like;

public interface LikeService {
	Like getLikeByID(Long id);
	
	Like saveLike(Like like);
	
	void deleteLike(Like like);
}
