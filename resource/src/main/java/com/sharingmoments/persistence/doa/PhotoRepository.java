package com.sharingmoments.persistence.doa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sharingmoments.persistence.model.Photo;
import com.sharingmoments.persistence.model.User;

public interface PhotoRepository extends PagingAndSortingRepository<Photo, Long> {
	Page<Photo> findByUser(User user, Pageable pageable);
}