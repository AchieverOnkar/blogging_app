package com.backend.blog.controllers;

import java.io.IOException;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.util.StreamUtils;
import org.springframework.util.StreamUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.blog.payloads.ApiResponse;
import com.backend.blog.payloads.PostDto;
import com.backend.blog.payloads.PostResponse;
import com.backend.blog.services.FileService;
import com.backend.blog.services.PostService;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("api/")
public class PostController {
	@Autowired
	private PostService postService;
	
	@Autowired
	private FileService fileService;
	@Value("${project.image}")
	private String path;
	

	// create post
	@PostMapping("/user/{userId}/category/{categoryId}/post")
	public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, @PathVariable Integer userId,
			@PathVariable Integer categoryId) {
		PostDto createdPost = this.postService.createPost(postDto, userId, categoryId);
		return new ResponseEntity<PostDto>(createdPost, HttpStatus.CREATED);
	}

	// update post
	@PutMapping("/post/{postId}")
	public ResponseEntity<PostDto> putMethodName(@RequestBody PostDto postDto, @PathVariable Integer postId) {
		PostDto updatePost = this.postService.updatePost(postDto, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.OK);
	}

	// get post by id
	@GetMapping("/post/{postId}")
	public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
		PostDto post = this.postService.getPostById(postId);
		return new ResponseEntity<PostDto>(post, HttpStatus.OK);
	}

	// get all post
	@GetMapping("/post/")
	public ResponseEntity<PostResponse> getAllPosts(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize,
			@RequestParam(value = "sortBy",defaultValue = "postId",required = false) String sortBy ,
	@RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir ){
		PostResponse postResponse = this.postService.getAllPosts(pageNumber, pageSize,sortBy,sortDir);
		return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
	}

	// get posts by user
	@GetMapping("/user/{userId}/post")
	public ResponseEntity<PostResponse> getPostsByUser(@PathVariable Integer userId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize) {
		PostResponse postsRes = this.postService.getPostsByUser(userId, pageNumber, pageSize);
		return new ResponseEntity<PostResponse>(postsRes, HttpStatus.OK);
	}

	// get posts by category
	@GetMapping("/category/{categoryId}/post")
	public ResponseEntity<PostResponse> getPostsByCategory(@PathVariable Integer categoryId,
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) Integer pageSize) {
		PostResponse postsByCategory = this.postService.getPostsByCategory(categoryId, pageNumber, pageSize);
		return new ResponseEntity<PostResponse>(postsByCategory, HttpStatus.OK);
	}
	
	//search post by title
	@GetMapping("/post/search/{keyword}")
	public ResponseEntity<List<PostDto>> searchPosts(@PathVariable String keyword) {
		List<PostDto> searchResults = this.postService.searchPosts(keyword);
		return new ResponseEntity<List<PostDto>>(searchResults,HttpStatus.OK);
	}
	

	// delete post
	@DeleteMapping("/post/{postId}")
	public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
		this.postService.deletePostById(postId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Post succefully deleted !!", true), HttpStatus.OK);
	}

	@PostMapping("/post/image/upload/{postId}")
	public ResponseEntity<PostDto> uploadFile(@RequestParam("image") MultipartFile file, @PathVariable Integer postId)
			throws IOException {
		PostDto post = this.postService.getPostById(postId);
		String fileName = this.fileService.uploadImage(path, file);
		post.setPostImage(fileName);
		PostDto updatePost = this.postService.updatePost(post, postId);
		return new ResponseEntity<PostDto>(updatePost, HttpStatus.CREATED);
	}
	
	@GetMapping("/post/image/")
	public void getImage(@RequestParam String fileName, HttpServletResponse response) throws IOException {
		InputStream inputStream = this.fileService.getResource(fileName, path);
		//set the response media type
		response.setContentType(MediaType.ALL_VALUE);
		//copy the inputstrem to response outputstream
		 StreamUtils.copy(inputStream, response.getOutputStream());
	}
	
	
	
}
