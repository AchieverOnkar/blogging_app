package com.backend.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.backend.blog.entities.Comment;
import com.backend.blog.entities.Post;
import com.backend.blog.entities.User;
import com.backend.blog.exceptions.ResourceNotFoundException;
import com.backend.blog.payloads.CommentDto;
import com.backend.blog.payloads.CommentResponse;
import com.backend.blog.payloads.PostDto;
import com.backend.blog.payloads.UserDto;
import com.backend.blog.repositories.CommentRepo;
import com.backend.blog.repositories.PostRepo;
import com.backend.blog.repositories.UserRepo;
import com.backend.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentRepo commentRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private PostRepo postRepo;

	@Override
	public CommentDto createComment(CommentDto commentDto,Integer userId, Integer postId) {
		//get user
		User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user","userId", userId));
		//get post
		Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post","postId", postId));
		//set user and post into the commentDto
		commentDto.setUser(this.modelMapper.map(user, UserDto.class));
		commentDto.setPost(this.modelMapper.map(post, PostDto.class));
		commentDto.setCommentDate(new Date());
		// comvert dto to obj
		Comment comment = this.modelMapper.map(commentDto, Comment.class);
		Comment savedComment = this.commentRepo.save(comment);
		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public CommentDto updateComment(CommentDto commentDto, Integer commentId) {
		// get the comment using id
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
		// set fields
		comment.setCommentContent(commentDto.getCommentContent());
		comment.setCommentDate(new Date());
		// save in db
		Comment savedComment = this.commentRepo.save(comment);

		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		// get comment obj
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
		// delete
		this.commentRepo.delete(comment);

	}

	@Override
	public CommentDto getCommentById(Integer commentId) {
		Comment comment = this.commentRepo.findById(commentId)
				.orElseThrow(() -> new ResourceNotFoundException("comment", "commentId", commentId));
		return this.modelMapper.map(comment, CommentDto.class);
	}

	@Override
	public CommentResponse getAllComments(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
		// create sort obj
		Sort sort = ((sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());

		// create pageabl
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Comment> page = this.commentRepo.findAll(p);

		// extract content
		List<Comment> content = page.getContent();
		// convert to dtos
		List<CommentDto> commentDtos = content.stream().map(com -> this.modelMapper.map(com, CommentDto.class))
				.collect(Collectors.toList());

		CommentResponse commentResponse = new CommentResponse(commentDtos, page.getNumber(), page.getSize(),
				page.getTotalElements(), page.getTotalPages(), page.isFirst(), page.isLast());
		return commentResponse;
	}

	@Override
	public CommentResponse getAllCommnetByUser(Integer userId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		// find comment by user
		// get user
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
		// create sort
		Sort sort = ((sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
		// create pageable
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);

		Page<Comment> page = this.commentRepo.findByUser(user, p);
		List<Comment> comments = page.getContent();
		// convert to dto
		List<CommentDto> commentDtos = comments.stream().map(com -> this.modelMapper.map(com, CommentDto.class))
				.collect(Collectors.toList());

		CommentResponse commentResponse = new CommentResponse(commentDtos, page.getNumber(), page.getSize(),
				page.getTotalElements(), page.getTotalPages(), page.isFirst(), page.isLast());
		return commentResponse;
	}

	@Override
	public CommentResponse getAllCommentByPost(Integer postId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDir) {
		// get post
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
		// create sort
		Sort sort = ((sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
		// create pageable
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		// find the comments
		Page<Comment> page = this.commentRepo.findByPost(post, p);
		List<Comment> comments = page.getContent();
		// convert
		List<CommentDto> commentDtos = comments.stream().map(com -> this.modelMapper.map(com, CommentDto.class))
				.collect(Collectors.toList());
		CommentResponse commentResponse = new CommentResponse(commentDtos, page.getNumber(), page.getSize(),
				page.getTotalElements(), page.getTotalPages(), page.isFirst(), page.isLast());
		return commentResponse;
	}

}
