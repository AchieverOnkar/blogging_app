package com.backend.blog.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.backend.blog.payloads.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	//this will handle all the exception related to resourceNotfound class
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
		//extract the msg from the exception object
		String  message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(apiResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BlogAPIException.class)
	public ResponseEntity<ApiResponse> blogAPIException(BlogAPIException e){
		String msg = e.getMessage();
		HttpStatus status = e.getStatus();
		ApiResponse apiResponse = new ApiResponse(msg,false);
		
		return new ResponseEntity<ApiResponse>(apiResponse,status);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgsNotValidExceptionHandler(MethodArgumentNotValidException ex){
		Map<String, String> response = new HashMap<>();
		//get the fieldname and valur from the ex
		//first get binding Result from the wrapper then get all error objects 
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			//need to typecast the error into fieldError
			 String fieldName = ((FieldError)error).getField(); //get filedName
			 String message = ((FieldError)error).getDefaultMessage();//and its message
			 //put the entry
			 response.put(fieldName, message);
		});
		return new ResponseEntity<Map<String, String>>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ApiResponse> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException ex){
		String message = ex.getMessage();
		ApiResponse response = new ApiResponse(message,false);
		return new ResponseEntity<ApiResponse>(response,HttpStatus.BAD_REQUEST);
	}

}
