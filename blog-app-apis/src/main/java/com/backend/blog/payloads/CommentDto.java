package com.backend.blog.payloads;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentDto {
	private Integer commentId;
	private String commentContent;
	private Date commentDate;
	private UserDto user;
	private PostDto post;
	

}
