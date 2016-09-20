package com.sharingmoments.core.persistence.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharingmoments.core.persistence.model.User;
import com.sharingmoments.core.persistence.model.UserImage;


public interface UserImageRepository extends JpaRepository<UserImage, Long> {
	@Override
	void delete(UserImage userImage);
    
    UserImage findByUser(User user);
}