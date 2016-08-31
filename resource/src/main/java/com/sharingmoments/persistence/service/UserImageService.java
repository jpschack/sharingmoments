package com.sharingmoments.persistence.service;

import com.sharingmoments.persistence.model.User;
import com.sharingmoments.persistence.model.UserImage;

public interface UserImageService {
	UserImage getUserImageByID(long id);
	
	UserImage getUserImageByUser(User user);
	
	UserImage saveUserImage(UserImage userImage);
	
	void deleteUserImage(UserImage userImage);
}
