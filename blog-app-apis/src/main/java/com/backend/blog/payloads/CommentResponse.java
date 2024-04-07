package com.backend.blog.payloads;

import java.util.List;

import com.backend.blog.entities.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CommentResponse {

	
	private List<CommentDto> comments;
	private Integer pageNumber;
	private Integer pageSize;
	private long totalElements;
	private Integer totalPages;
	private boolean isFirst;
	private boolean isLast;
	
	public CommentResponse(List<CommentDto> comments, Integer pageNumber, Integer pageSize, long totalElements,
			Integer totalPages, boolean isFirst, boolean isLast) {
		super();
		this.comments = comments;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.isFirst = isFirst;
		this.isLast = isLast;
	}
	
	
	
}
