package com.sharingmoments.resource.persistence.service;

import com.sharingmoments.resource.persistence.model.Comment;

public interface CommentService {
	
	Comment getCommentByID(Long id);
	
	Comment saveComment(Comment comment);
	
	void deleteComment(Comment comment);
}
