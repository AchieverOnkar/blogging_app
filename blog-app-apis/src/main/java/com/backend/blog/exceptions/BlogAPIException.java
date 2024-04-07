package com.backend.blog.exceptions;

import java.io.IOException;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class BlogAPIException extends RuntimeException{
	
	private HttpStatus status;
	private String message;
	
	
	public BlogAPIException(HttpStatus status, String message) {
		super(message);
		this.status = status;
		this.message = message;
	}
	
	

}
