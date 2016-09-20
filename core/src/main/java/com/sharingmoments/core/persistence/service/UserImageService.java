package com.sharingmoments.core.persistence.service;

import com.sharingmoments.core.persistence.model.User;
import com.sharingmoments.core.persistence.model.UserImage;

public interface UserImageService {
	UserImage getUserImageByID(long id);
	
	UserImage getUserImageByUser(User user);
	
	UserImage saveUserImage(UserImage userImage);
	
	void deleteUserImage(UserImage userImage);
}
