package com.sharingmoments.resource.persistence.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharingmoments.resource.persistence.model.Comment;


public interface CommentRepository extends JpaRepository<Comment, Long> {
	
	@Override
    void delete(Comment comment);
}
