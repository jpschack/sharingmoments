package com.sharingmoments.core.persistence.doa;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sharingmoments.core.persistence.model.Event;
import com.sharingmoments.core.persistence.model.Photo;
import com.sharingmoments.core.persistence.model.User;


public interface PhotoRepository extends PagingAndSortingRepository<Photo, UUID> {
	Page<Photo> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
	
	Page<Photo> findByEventOrderByCreatedAtDesc(Event event, Pageable pageable);
}