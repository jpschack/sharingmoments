package com.sharingmoments.resource.persistence.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharingmoments.resource.persistence.doa.CommentRepository;
import com.sharingmoments.resource.persistence.model.Comment;


@Service
@Transactional
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentRepository repository;

	@Override
	public Comment getCommentByID(Long id) {
		return repository.getOne(id);
	}

	@Override
	public Comment saveComment(Comment comment) {
		return repository.save(comment);
	}

	@Override
	public void deleteComment(Comment comment) {
		repository.delete(comment);
	}
}
