package com.backend.blog.payloads;

import java.util.Date;

import com.backend.blog.entities.Category;
import com.backend.blog.entities.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostDto {
	private Integer postId;
	private String postTitle;
	private String postContent;
	private String postImage;
	private Date postDate;
	private UserDto user;
	private CategoryDto category;

}
