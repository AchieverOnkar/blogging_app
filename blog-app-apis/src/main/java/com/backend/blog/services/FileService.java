package com.backend.blog.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	
	//to uplode file
	String uploadImage(String path,MultipartFile file) throws IOException;
	
	InputStream getResource(String fileName, String path) throws FileNotFoundException;

}
