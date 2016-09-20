package com.sharingmoments.core.persistence.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharingmoments.core.persistence.model.Like;



public interface LikeRepository extends JpaRepository<Like, Long> {
	
	@Override
    void delete(Like like);
}
