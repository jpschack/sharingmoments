package com.sharingmoments.core.persistence.service;

import com.sharingmoments.core.persistence.model.Comment;

public interface CommentService {
	
	Comment getCommentByID(Long id);
	
	Comment saveComment(Comment comment);
	
	void deleteComment(Comment comment);
}
