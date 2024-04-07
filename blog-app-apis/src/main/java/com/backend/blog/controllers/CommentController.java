package com.backend.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.blog.payloads.ApiResponse;
import com.backend.blog.payloads.CommentDto;
import com.backend.blog.payloads.CommentResponse;
import com.backend.blog.services.CommentService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/")
public class CommentController {
	@Autowired
	private CommentService commentService;

	@PostMapping("user/{userId}/post/{postId}/comment/")
	public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable Integer userId,
			@PathVariable Integer postId) {
		CommentDto savedComment = this.commentService.createComment(commentDto, userId, postId);
		return new ResponseEntity<CommentDto>(savedComment, HttpStatus.CREATED);
	}

	@PutMapping("comment/{commentId}")
	public ResponseEntity<CommentDto> updateComment(@PathVariable Integer commentId,
			@RequestBody CommentDto commentDto) {
		CommentDto updatedComment = this.commentService.updateComment(commentDto, commentId);
		return new ResponseEntity<CommentDto>(updatedComment, HttpStatus.OK);
	}

	@DeleteMapping("comment/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId) {
		this.commentService.deleteComment(commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment deleted succesfully !!", true), HttpStatus.OK);
	}

	@GetMapping("comment/{commentId}")
	public ResponseEntity<CommentDto> getMethodName(@PathVariable Integer commentId) {
		CommentDto comment = this.commentService.getCommentById(commentId);
		return new ResponseEntity<CommentDto>(comment, HttpStatus.OK);
	}

	@GetMapping("comment/")
	public ResponseEntity<CommentResponse> getAllComments(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "commentDate", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		CommentResponse comments = this.commentService.getAllComments(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<CommentResponse>(comments, HttpStatus.OK);
	}

	@GetMapping("/user/{userId}/comment/")
	public ResponseEntity<CommentResponse> getAllCommentsByUser(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "commentDate", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@PathVariable Integer userId) {
		CommentResponse commentResponse = this.commentService.getAllCommnetByUser(userId, pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<CommentResponse>(commentResponse,HttpStatus.OK);
	}
	
	@GetMapping("/post/{postId}/comment/")
	public ResponseEntity<CommentResponse> getAllCommentsByPost(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = "commentDate", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
			@PathVariable Integer postId) {
		CommentResponse commentResponse = this.commentService.getAllCommentByPost(postId, pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<CommentResponse>(commentResponse,HttpStatus.OK);
	}

}
