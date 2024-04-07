package com.backend.blog.services;

import java.util.List;

import com.backend.blog.entities.Category;
import com.backend.blog.entities.Post;
import com.backend.blog.entities.User;
import com.backend.blog.payloads.PostDto;
import com.backend.blog.payloads.PostResponse;

public interface PostService {
	//create post
	PostDto createPost(PostDto postDto,Integer userId, Integer categoryId);
	//update post
	PostDto updatePost(PostDto postDto,Integer postId);
	//get post by id
	PostDto getPostById(Integer postId);
	//get all posts
	PostResponse getAllPosts(Integer pageNumber,Integer pageSize, String sortBy, String sortDir);
	//delete post by id
	void deletePostById(Integer postId);
	//get posts by user
	PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize);
	//get posts by category
	PostResponse getPostsByCategory(Integer categoryId,Integer pageNumber, Integer pageSize);
	//search posts
	List<PostDto> searchPosts(String keyword);

}
