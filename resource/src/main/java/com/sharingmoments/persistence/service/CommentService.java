package com.sharingmoments.persistence.service;

import com.sharingmoments.persistence.model.Comment;

public interface CommentService {
	
	Comment getCommentByID(Long id);
	
	Comment saveComment(Comment comment);
	
	void deleteComment(Comment comment);
}
