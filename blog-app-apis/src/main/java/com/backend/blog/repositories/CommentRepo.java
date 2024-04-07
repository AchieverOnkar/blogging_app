package com.backend.blog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.blog.entities.Comment;
import com.backend.blog.entities.Post;
import com.backend.blog.entities.User;

public interface CommentRepo extends JpaRepository<Comment, Integer>{

	Page<Comment> findByUser(User user, Pageable p);
	Page<Comment> findByPost(Post post,Pageable p);

}
