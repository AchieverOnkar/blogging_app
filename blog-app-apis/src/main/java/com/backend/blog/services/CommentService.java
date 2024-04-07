package com.backend.blog.services;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

import com.backend.blog.payloads.CommentDto;
import com.backend.blog.payloads.CommentResponse;

public interface CommentService {
	//create comment
	CommentDto createComment(CommentDto commentDto,Integer userId, Integer postId);
	//update comment
	CommentDto updateComment(CommentDto commentDto,Integer commentId);
	//delete comment
	void deleteComment(Integer commentId);
	//get single comment
	CommentDto getCommentById(Integer commentId);
	//get all comment
	CommentResponse getAllComments(Integer pageNumber,Integer pageSize,String sortBy,String sortDir);
	//get comments by user
	CommentResponse getAllCommnetByUser(Integer userId,Integer pageNumber,Integer pageSize, String sortBy, String sortDir);
	//get comments by post
	CommentResponse getAllCommentByPost(Integer postId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

}
