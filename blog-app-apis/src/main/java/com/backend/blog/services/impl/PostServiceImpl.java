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

import com.backend.blog.entities.Category;
import com.backend.blog.entities.Post;
import com.backend.blog.entities.User;
import com.backend.blog.exceptions.ResourceNotFoundException;
import com.backend.blog.payloads.PostDto;
import com.backend.blog.payloads.PostResponse;
import com.backend.blog.repositories.CategoryRepo;
import com.backend.blog.repositories.PostRepo;
import com.backend.blog.repositories.UserRepo;
import com.backend.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostRepo postRepo;
	@Autowired
	private UserRepo useRepo;
	@Autowired
	private CategoryRepo categoryRepo;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
		// get the user using id
		User user = this.useRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
		Category category = this.categoryRepo.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId ", categoryId));

		// convert dto to entity
		Post post = this.modelMapper.map(postDto, Post.class);

		// set the fields
		post.setPostImage("default.jpg");
		post.setPostDate(new Date());
		post.setCategory(category);
		post.setUser(user);

		// save the post
		Post savedPost = this.postRepo.save(post);

		return this.modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		// get post
		Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post","postId", postId));
		// set field for post
		post.setPostTitle(postDto.getPostTitle());
		post.setPostContent(postDto.getPostContent());
		post.setPostDate(new Date());
		post.setPostImage(postDto.getPostImage());
		// save the post
		Post savedPost = this.postRepo.save(post);

		return this.modelMapper.map(savedPost, PostDto.class);
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post = this.postRepo.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
		return this.modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostResponse getAllPosts(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
		//create sort object while accorrding to sort direction
		Sort sort = ((sortDir.equalsIgnoreCase("asc"))? Sort.by(sortBy).ascending():Sort.by(sortBy).descending());
		//create pagable object using pagerequest of method
		Pageable p = PageRequest.of(pageNumber , pageSize, sort);
		//get posts
		Page<Post> page = this.postRepo.findAll(p);
		//get the content from page
		
		List<Post> posts = page.getContent();
		
		// convert list of post to dtos
		List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		
		PostResponse pageRes = new PostResponse();
		//set the fields
		pageRes.setContent(postDtos);
		pageRes.setPageNumber(page.getNumber());
		pageRes.setPageSize(page.getSize());
		pageRes.setTotalElements(page.getTotalElements());
		pageRes.setTotalPages(page.getTotalPages());
		pageRes.setLast(page.isLast());
		pageRes.setFirst(page.isFirst());
		return pageRes;
	}

	@Override
	public void deletePostById(Integer postId) {
		
		Post post = this.postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("post", "postId", postId));
	    this.postRepo.delete(post);
	}

	@Override
	public PostResponse getPostsByUser(Integer userId,Integer pageNumber, Integer pageSize) {
		//create pagaeble obj
		Pageable p = PageRequest.of(pageNumber, pageSize);
		User user = this.useRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "userId", userId));
		Page<Post> page = this.postRepo.findByUser(user,p);
		//retrive the content
		List<Post> posts = page.getContent();
		List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		PostResponse postRes = new PostResponse();
		postRes.setContent(postDtos);
		postRes.setPageNumber(page.getNumber());
		postRes.setPageSize(page.getSize());
		postRes.setTotalElements(page.getTotalElements());
		postRes.setTotalPages(page.getTotalPages());
		postRes.setFirst(page.isFirst());
		postRes.setLast(page.isLast());
		
		return postRes;
	}

	@Override
	public PostResponse getPostsByCategory(Integer categoryId,Integer pageNumber, Integer pageSize) {
		//create pagaeble
		Pageable p = PageRequest.of(pageNumber,pageSize);
		Category category = this.categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
		Page<Post> page = this.postRepo.findByCategory(category,p);
		//extract content from page
		List<Post> posts = page.getContent();
		List<PostDto> postDtos = posts.stream().map(post -> this.modelMapper.map(post, PostDto.class))
				.collect(Collectors.toList());
		//create postResp
		PostResponse postRes = new PostResponse();
		postRes.setContent(postDtos);
		postRes.setPageNumber(page.getNumber());
		postRes.setPageSize(page.getSize());
		postRes.setTotalElements(page.getTotalElements());
		postRes.setTotalPages(page.getTotalPages());
		postRes.setFirst(page.isFirst());
		postRes.setLast(page.isLast());
		return postRes;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		
		List<Post> results = this.postRepo.findByPostTitleContaining(keyword);
        List<PostDto> resultDtos = results.stream().map(post -> this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		
		return resultDtos;
	}

}
