package com.sharingmoments.resource.persistence.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharingmoments.resource.persistence.model.Like;



public interface LikeRepository extends JpaRepository<Like, Long> {
	
	@Override
    void delete(Like like);
}
