package com.sharingmoments.resource.persistence.service;

import com.sharingmoments.resource.persistence.model.User;
import com.sharingmoments.resource.persistence.model.UserImage;

public interface UserImageService {
	UserImage getUserImageByID(long id);
	
	UserImage getUserImageByUser(User user);
	
	UserImage saveUserImage(UserImage userImage);
	
	void deleteUserImage(UserImage userImage);
}
