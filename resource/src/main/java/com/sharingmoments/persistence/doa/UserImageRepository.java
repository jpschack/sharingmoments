package com.sharingmoments.persistence.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharingmoments.persistence.model.User;
import com.sharingmoments.persistence.model.UserImage;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
	
    @Override
    void delete(UserImage userImage);
    
    UserImage findByUser(User user);
}